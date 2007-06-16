#!/bin/sh
#
# $Id: scrape.sh,v 1.4 2007-06-16 14:30:45 gunter Exp $
#
# requires: lynx gawk
#
. program_guide.conf

if [ "$#" -ne "1" ]; then
    echo "usage: `basename $0` PROGRAM_ID"
    exit 1
fi

ID=$1

SQL=\
"SELECT name, url
FROM program
WHERE id = ${ID}"

DATA=`mysql -u ${MYSQLUSER} -p${MYSQLPASSWORD} -s --skip-column-names ${DATABASE} -e "${SQL}" | tr '\t' '|'`
if [ "$?" -ne "0" ]; then
    exit 1
fi

PROGRAM="`echo ${DATA} | cut -d "|" -f 1`"
URL="`echo ${DATA} | cut -d "|" -f 2`"
DUMP=`mktemp /tmp/scrape.dump.XXXXXX`
RAW=`mktemp /tmp/scrape.raw.XXXXXX`
DATA=`mktemp /tmp/scrape.data.XXXXXX`
BEFORE=`mktemp /tmp/scrape.before.XXXXXX`
AFTER=`mktemp /tmp/scrape.after.XXXXXX`
FIELDS="5 8 13 11 255 1024"
SERIAL_NUMBER=0

. hacks

echo "${PROGRAM} => ${DUMP}"
echo "${PROGRAM} => ${RAW}"
echo "${PROGRAM} => ${DATA}"

EPISODE_SQL=\
"SELECT *
FROM episode
WHERE program_id = ${ID}
ORDER BY program_id, season, number"

mysql -u ${MYSQLUSER} -p${MYSQLPASSWORD} --skip-column-names ${DATABASE} \
    -e "${EPISODE_SQL}" > ${BEFORE}
    
lynx -dont_wrap_pre -dump "${URL}" > ${DUMP}
cat ${DUMP} | \
    # Find lines containing episode data.  
    egrep -e '^([[:space:]]*\<[[:alnum:]]+\.[[:space:]]+|[[:space:]]+)\<[[:alnum:]]+-[([:digit:]|[:space:][:digit:])]' | \
    # Treat data as fixed length and ignore the first field. (Hack?)
    ${AWK} -v FIELDWIDTHS="${FIELDS}" '{print $2 "|" $3 "|" $4 "|" $5}' | \
    # Convert spaces to _ to make the for loop work correctly.
    tr ' ' '_' > ${RAW}

for line in `cat ${RAW}`
do
    SEASON=`echo "${line}" | cut -d "|" -f 1 | cut -d "-" -f 1 | tr -d '_'`
    EPISODE=`echo "${line}" | cut -d "|" -f 1 | cut -d "-" -f 2 | tr -d '_'`
    PRODUCTION_CODE=`echo "${line}" | cut -d "|" -f 2 | tr -d '_'`
    DATE=`echo "${line}" | cut -d "|" -f 3 | tr -d '_' | awk '{printf "%07s", $1}'`
    TITLE=`echo "${line}" | cut -d "|" -f 4 | tr '_' ' ' | sed 's/^[[:space:]]*//' | sed 's/\[.*\]//'`
    LINK_NUMBER=`echo "${line}" | cut -d "|" -f 4 | tr '_' ' ' | sed 's/^[[:space:]]*//' | sed 's/\].*//' | tr -d '['`
    SUMMARY_LINK=`cat ${DUMP} | grep "^[[:space:]]*${LINK_NUMBER}\.[[:space:]]*http" | sed "s/.*${LINK_NUMBER}\. *//"`
    SERIAL_NUMBER=`expr ${SERIAL_NUMBER} + 1`

    # The year is two digits and MySQL adds 2000 for 00-69 
    # and adds 1900 for 70-99.  That is undesirable in this context.
    YEARPART=`echo ${DATE} | cut -c 6-8`
    if [ "${YEARPART}" -ge "30" ]; then
        YEAR=19${YEARPART}
    else # < 30
        YEAR=20${YEARPART}
    fi
    FULLDATE=`echo ${DATE} | cut -c 1-5`${YEAR}
    if [ "${DATE}" != "0000000" ]; then
        ORIGINAL_AIR_DATE=`mysql -u ${MYSQLUSER} -p${MYSQLPASSWORD} -s --skip-column-names -e "SELECT STR_TO_DATE('${FULLDATE}', '%d%b%Y')"`
    else
        ORIGINAL_AIR_DATE=""
    fi
    # If any variable is empty, set it to null (\N).
    if [ -z "${SEASON}" ]; then
        SEASON=\\N
    fi 
    if [ -z "${EPISODE}" ]; then
        EPISODE=\\N
    fi 
    if [ -z "${PRODUCTION_CODE}" ]; then
        PRODUCTION_CODE=\\N
    fi 
    if [ -z "${ORIGINAL_AIR_DATE}" ]; then
        ORIGINAL_AIR_DATE=\\N
    fi 
    if [ -z "${TITLE}" ]; then
       TITLE=\\N
    fi 
    if [ -z "${SUMMARY_LINK}" ]; then
       SUMMARY_LINK=\\N
    fi 

    echo "${PROGRAM} => [${SEASON}|${EPISODE}|${PRODUCTION_CODE}|${ORIGINAL_AIR_DATE}|${TITLE}|${SERIAL_NUMBER}|${SUMMARY_LINK}]"
    echo "${ID}|${SEASON}|${EPISODE}|${PRODUCTION_CODE}|${ORIGINAL_AIR_DATE}|${TITLE}|${SERIAL_NUMBER}|${SUMMARY_LINK}" >> ${DATA}
done

EPISODE_COUNT=`wc -l ${DATA} | tr -s ' ' | cut -d " "  -f 2`
echo "${PROGRAM} => ${EPISODE_COUNT} episodes"
if [ "${EPISODE_COUNT}" -eq "0" ]; then
    echo "${PROGRAM} => Nothing to load"
    exit 0
fi

DELETE=\
"DELETE FROM episode
WHERE program_id = ${ID}"
mysql -u ${MYSQLUSER} -p${MYSQLPASSWORD} ${DATABASE} -e "${DELETE}"
if [ "$?" -ne "0" ]; then
    exit 1
fi 

LOAD=\
"LOAD DATA LOCAL INFILE '${DATA}' REPLACE
INTO TABLE episode FIELDS TERMINATED BY '|'
(program_id, season, number, production_code, original_air_date, title, serial_number, summary_url)"
mysql --local-infile=1 -u ${MYSQLUSER} -p${MYSQLPASSWORD} ${DATABASE} -e "${LOAD}"
if [ "$?" -ne "0" ]; then
    exit 1
fi 

echo "${PROGRAM} => Loaded ${DATA}"

UPDATE_DATE=`date '+%y-%m-%d %H:%M:%S'`

UPDATE=\
"UPDATE program
SET last_update = '${UPDATE_DATE}'
WHERE id = ${ID}"
mysql -u ${MYSQLUSER} -p${MYSQLPASSWORD} ${DATABASE} -e "${UPDATE}"
if [ "$?" -ne "0" ]; then
    exit 1
fi 

echo "${PROGRAM} => Updated timestamp to ${UPDATE_DATE}"

DATE_SQL=\
"UPDATE episode
SET original_air_date = NULL
WHERE original_air_date = '0000-00-00'
AND program_id = ${ID}"
mysql -vv -u ${MYSQLUSER} -p${MYSQLPASSWORD} ${DATABASE} -e "${DATE_SQL}"
if [ "$?" -ne "0" ]; then
    exit 1
fi

mysql -u ${MYSQLUSER} -p${MYSQLPASSWORD} --skip-column-names ${DATABASE} \
    -e "${EPISODE_SQL}" > ${AFTER}

# Compare the line count of DATA to AFTER to determine if data was lost
# when it was inserted.  This could happen if the data wasn't parsed correctly
# or if there was an error in the data (i.e., two episodes with the same primary
# key).
EPISODE_COUNT2=`wc -l ${AFTER} | tr -s ' ' | cut -d " "  -f 2`
if [ "${EPISODE_COUNT}" -ne "${EPISODE_COUNT2}" ]; then
    echo "${PROGRAM} => Not all records were inserted: expected ${EPISODE_COUNT}, but got ${EPISODE_COUNT2}"
fi

diff -u ${BEFORE} ${AFTER}

rm -f ${DUMP} ${RAW} ${DATA} ${BEFORE} ${AFTER}

exit 0
