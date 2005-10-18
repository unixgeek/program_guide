#!/bin/sh
#
# $Id: scrape.sh,v 1.7 2005-10-18 02:09:17 gunter Exp $
#
# requires: html2text
#
. program_guide.config

if [ "$#" -ne "3" ]; then
    echo "usage: `basename $0` id program url"
    exit 1
fi

ID="${1}"
PROGRAM="${2}"
URL="${3}"
HTML=`mktemp /tmp/scrape.html.XXXXXX`
DATA=`mktemp /tmp/scrape.data.XXXXXX`

fetch -q -o ${HTML} "${URL}"
if [ "$?" -ne "0" ]; then
    echo "fetch failed."
    exit 1
fi

dos2unix ${HTML}
if [ "$?" -ne "0" ]; then
    echo "dos2unix failed."
    exit 1
fi

echo "${PROGRAM} => ${HTML}"
echo "${PROGRAM} => ${DATA}"

# Possible line formats:
#                 1|              2|  3|    4|   5|    6
# 1) SEASON-EPISODE|PRODUCTION_CODE|DAY|MONTH|YEAR|TITLE
#                 1|              2|   3|
# 2) SEASON-EPISODE|PRODUCTION_CODE|TITLE
#                 1|  2|    3|   4|   5|
# 3) SEASON-EPISODE|DAY|MONTH|YEAR|TITLE
#                 1|   2|
# 4) SEASON-EPISODE|TITLE

# Possible shorter expression for matching: '(\<[[:alnum:]]+\.[[:space:]]+|[[:space:]]+)\<[[:alnum:]]-'
for line in `html2text -nobs ${HTML} | egrep -e '(\<[[:alnum:]]+\.[[:space:]]+|[[:space:]]+)\<[[:alnum:]]-[([:digit:]|[:space:][:digit:])]' | sed -E 's/^[[:space:]]*[[:digit:]]+\.//' | tr -s ' ' | sed 's/^ //' | sed 's/- /-/' | tr ' ' '|'`
do
    SEASON=`echo "${line}" | cut -d "|" -f 1 | cut -d "-" -f 1`
    EPISODE=`echo "${line}" | cut -d "|" -f 1 | cut -d "-" -f 2`
    PRODUCTION_CODE=`echo "${line}" | cut -d "|" -f 2`
    DAY=`echo "${line}" | cut -d "|" -f 3`
    MONTH=`echo "${line}" | cut -d "|" -f 4`
    YEAR=`echo "${line}" | cut -d "|" -f 5`
    TITLE=`echo "${line}" | cut -d "|" -f 6 | tr '_' ' '`
    AIR_DATE=`date -j -f %y%b%d ${YEAR}${MONTH}${DAY} +%y%m%d 2> /dev/null`
    CASE=1

    # If TITLE and DAY are empty, then PRODUCTION_CODE and AIR_DATE
    # are missing from the data.
    if [ -z "${DAY}" ] && [ -z "${TITLE}" ]; then
        PRODUCTION_CODE=null
        AIR_DATE=null
        TITLE=`echo "${line}" | cut -d "|" -f 2`
        CASE=4
    # If TITLE is empty, then PRODUCTION_CODE or AIR_DATE is missing from 
    # the data.
    elif [ -z "${TITLE}" ]; then
        # Try processing a date. If date is successful, the PRODUCITON_CODE
        # is the missing field; otherwise, AIR_DATE is missing.
        DAY=`echo "${line}" | cut -d "|" -f 2`
        MONTH=`echo "${line}" | cut -d "|" -f 3`
        YEAR=`echo "${line}" | cut -d "|" -f 4`
        AIR_DATE=`date -j -f %y%b%d ${YEAR}${MONTH}${DAY} +%y%m%d 2> /dev/null`
        if [ "$?" -ne "0" ]; then
            AIR_DATE=null
            TITLE=`echo "${line}" | cut -d "|" -f 3 | tr '_' ' '`
            CASE=2
        else
            PRODUCTION_CODE=null
            TITLE=`echo "${line}" | cut -d "|" -f 5 | tr '_' ' '`
            CASE=3
        fi
    fi

    echo "${PROGRAM} => (${CASE}) [${SEASON}|${EPISODE}|${PRODUCTION_CODE}|${AIR_DATE}|${TITLE}]"
    echo "${ID}|${SEASON}|${EPISODE}|${PRODUCTION_CODE}|${AIR_DATE}|${TITLE}"  >> ${DATA}
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

exit 0
