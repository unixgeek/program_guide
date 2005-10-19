#!/bin/sh
#
# $Id: scrape.sh,v 1.10 2005-10-19 15:05:07 gunter Exp $
#
# requires: lynx
#
. program_guide.config

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

echo "${PROGRAM} => ${RAW}"
echo "${PROGRAM} => ${DATA}"

# Dump the page to stdout.
lynx -nolist -dont_wrap_pre -dump "${URL}" | \
    # Find lines containing episode data.  
    egrep -e '^([[:space:]]*\<[[:alnum:]]+\.[[:space:]]+|[[:space:]]+)\<[[:alnum:]]-[([:digit:]|[:space:][:digit:])]' | \
    # Treat data as fixed length and ignore the first field. (Hack?)
    awk -v FIELDWIDTHS="${FIELDS}" '{print $2 "|" $3 "|" $4 "|" $5}' | \
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

    echo "${PROGRAM} => [${SEASON}|${EPISODE}|${PRODUCTION_CODE}|${ORIGINAL_AIR_DATE}|${TITLE}]"
    echo "${ID}|${SEASON}|${EPISODE}|${PRODUCTION_CODE}|${ORIGINAL_AIR_DATE}|${TITLE}" >> ${DATA}
done

LOAD=\
"LOAD DATA LOCAL INFILE '${DATA}' REPLACE
INTO TABLE episode FIELDS TERMINATED BY '|'
(program_id, season, number, production_code, original_air_date, title)"
mysql ${DATABASE} -e "${LOAD}"
if [ "$?" -ne "0" ]; then
    exit 1
fi 
echo "${PROGRAM} => Loaded ${DATA}."

UPDATE_DATE=`date '+%y-%m-%d %H:%M:%S'`

SQL=\
"UPDATE program
SET last_update = '${UPDATE_DATE}'
WHERE id = ${ID}"
mysql ${DATABASE} -e "${SQL}"
if [ "$?" -ne "0" ]; then
    exit 1
fi 

echo "${PROGRAM} => Updated timestamp to ${UPDATE_DATE}"

rm -f ${RAW} ${DATA}

exit 0
