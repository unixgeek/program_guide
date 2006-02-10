#!/bin/sh
#
# $Id: scrape.sh,v 1.20 2006-02-10 01:52:44 gunter Exp $
#
# requires: lynx gawk
#
. ${HOME}/.program_guide.conf

if [ "$#" -ne "3" ]; then
    echo "usage: `basename $0` id program url"
    exit 1
fi

ID="${1}"
PROGRAM="${2}"
URL="${3}"
RAW=`mktemp /tmp/scrape.raw.XXXXXX`
DATA=`mktemp /tmp/scrape.data.XXXXXX`
FIELDS="5 8 13 11 255"
SERIAL_NUMBER=0

echo "${PROGRAM} => ${RAW}"
echo "${PROGRAM} => ${DATA}"

# Dump the page to stdout.
lynx -nolist -dont_wrap_pre -dump "${URL}" | \
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
    DATE=`echo "${line}" | cut -d "|" -f 3 | tr -d '_'`
    ORIGINAL_AIR_DATE=`date -j -f %d%b%y ${DATE} +%y%m%d 2> /dev/null`
    TITLE=`echo "${line}" | cut -d "|" -f 4 | tr '_' ' ' | sed 's/^[[:space:]]*//'`
    SERIAL_NUMBER=`expr ${SERIAL_NUMBER} + 1`

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

    echo "${PROGRAM} => [${SEASON}|${EPISODE}|${PRODUCTION_CODE}|${ORIGINAL_AIR_DATE}|${TITLE}|${SERIAL_NUMBER}]"
    echo "${ID}|${SEASON}|${EPISODE}|${PRODUCTION_CODE}|${ORIGINAL_AIR_DATE}|${TITLE}|${SERIAL_NUMBER}" >> ${DATA}
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
(program_id, season, number, production_code, original_air_date, title, serial_number)"
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

rm -f ${RAW} ${DATA}

exit 0
