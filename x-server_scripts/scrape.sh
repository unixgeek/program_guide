#
# $Id: scrape.sh,v 1.2 2005-10-17 01:42:50 gunter Exp $
#

if [ "$#" -ne "1" ]; then
    echo "usage: `basename $0` url"
    exit 1
fi

URL="${1}"
TMP=`mktemp /tmp/scrape.html.XXXXXX`
SQL=`mktemp /tmp/scrape.sql.XXXXXX`

fetch -o ${TMP} "${URL}"
if [ "$?" -ne "0" ]; then
    echo "fetch failed."
    exit 1
fi

dos2unix ${TMP}
if [ "$?" -ne "0" ]; then
    echo "dos2unix failed."
    exit 1
fi

PROGRAM=`sgrep '("<title>" .. "</title>")' ${TMP} | sed 's/<title>//' | sed 's/<\/title>//' | sed 's/ (a Titles and Air Dates Guide)//'`

# Verify program doesn't exist yet.
# mysql something

echo "INSERT INTO program (name) VALUES ('${PROGRAM}');" >> ${SQL} 

for line in `html2text -nobs ${TMP} | tr -s ' ' | grep -e '^ [[:digit:]]' | sed 's/^ //' | sed 's/- /-/' | tr ' ' '|'`
do
    SEASON=`echo "${line}" | cut -d "|" -f 2 | cut -d "-" -f 1`
    EPISODE=`echo "${line}" | cut -d "|" -f 2 | cut -d "-" -f 2`
    PRODUCTION_CODE=`echo "${line}" | cut -d "|" -f 3`
    DAY=`echo "${line}" | cut -d "|" -f 4`
    MONTH=`echo "${line}" | cut -d "|" -f 5`
    YEAR=`echo "${line}" | cut -d "|" -f 6`
    TITLE=`echo "${line}" | cut -d "|" -f 7 | tr '_' ' ' | sed "s/'/''/g"`
    AIR_DATE=`date -j -f %y%b%d ${YEAR}${MONTH}${DAY} +%y%m%d`
    if [ "$?" -ne "0" ]; then
        echo "date failed."
    fi
 
    # If TITLE and YEAR are empty, then PRODUCTION_CODE and AIR_DATE
    # are missing from the data.
    if [ -z "${DAY}" ] && [ -z "${TITLE}" ]; then
        PRODUCTION_CODE=null
        AIR_DATE=null
        TITLE=`echo "${line}" | cut -d "|" -f 3`
    # If TITLE is empty, then PRODUCTION_CODE or AIR_DATE is missing from 
    # the data.
    elif [ -z "${TITLE}" ]; then
        # Try processing a date. If date is successful, the PRODUCITON_CODE
        # is the missing field; otherwise, AIR_DATE is missing.
        DAY=`echo "${line}" | cut -d "|" -f 3`
        MONTH=`echo "${line}" | cut -d "|" -f 4`
        YEAR=`echo "${line}" | cut -d "|" -f 5`
        AIR_DATE=`date -j -f %y%b%d ${YEAR}${MONTH}${DAY} +%y%m%d`
        if [ "$?" -ne "0" ]; then
            echo "date failed."
            AIR_DATE=null
            TITLE=`echo "${line}" | cut -d "|" -f 4 | tr '_' ' ' | sed "s/'/''/g"`
        else
            PRODUCTION_CODE=null
            TITLE=`echo "${line}" | cut -d "|" -f 6 | tr '_' ' ' | sed "s/'/''/g"`
        fi
    fi

    echo "=>${PROGRAM}"
    echo "==> ${SEASON}"
    echo "==> ${EPISODE}"
    echo "==> ${PRODUCTION_CODE}"
    echo "==> ${AIR_DATE}"
    echo "==> ${TITLE}"
    echo "INSERT INTO episode VALUES (last_insert_id(), '${SEASON}', ${EPISODE}, '${PRODUCTION_CODE}', '${AIR_DATE}', '${TITLE}');" >> ${SQL}
done

# mysql program_guide < ${SQL}
less -S ${SQL}
exit 0
