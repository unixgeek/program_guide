#
# $Id: scrape.sh,v 1.1 2005-10-15 04:35:10 gunter Exp $
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
 
    # If TITLE is empty, then production code or date is probably missing from the data.
    if [ -z "${TITLE}" ]; then
        PRODUCTION_CODE=null
        DAY=01
        MONTH=Jan
        YEAR=00
        TITLE="FIXME: INCOMPLETE DATA"
        #DAY=`echo "${line}" | cut -d "|" -f 3`
        #MONTH=`echo "${line}" | cut -d "|" -f 4`
        #YEAR=`echo "${line}" | cut -d "|" -f 5`
        #TITLE=`echo "${line}" | cut -d "|" -f 6 | tr '_' ' '`
    fi

    AIR_DATE=`date -j -f %y%b%d ${YEAR}${MONTH}${DAY} +%y%m%d`
    if [ "$?" -ne "0" ]; then
        echo "date failed."
        continue
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

exit 0
