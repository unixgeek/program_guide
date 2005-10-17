#
# $Id: scrape.sh,v 1.4 2005-10-17 17:51:19 gunter Exp $
#
# requires: html2text sgrep
#
. program_guide.config

if [ "$#" -ne "2" ]; then
    echo "usage: `basename $0` id url"
    exit 1
fi

ID="${1}"
URL="${2}"
#HTML=`mktemp /tmp/scrape.html.XXXXXX`
HTML=/tmp/scrape.html.VLJ0lp
DATA=`mktemp /tmp/scrape.data.XXXXXX`

#fetch -q -o ${HTML} "${URL}"
if [ "$?" -ne "0" ]; then
    echo "fetch failed."
    exit 1
fi

dos2unix ${HTML}
if [ "$?" -ne "0" ]; then
    echo "dos2unix failed."
    exit 1
fi

PROGRAM=`sgrep '("<title>" .. "</title>")' ${HTML} | sed 's/<title>//' | sed 's/<\/title>//' | sed 's/ (a Titles and Air Dates Guide)//'`

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
for line in `html2text -nobs ${HTML} | grep -e '[[:alnum:]]-[([:digit:]|[:space:][:digit:])]' | sed -E 's/^[[:space:]]+[[:digit:]]+\.//' | tr -s ' ' | sed 's/^ //' | sed 's/- /-/' | tr ' ' '|'`
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
