#!/bin/sh
#
# $Id: find_outdated.sh,v 1.3 2006-03-22 04:56:13 gunter Exp $
#
# requires: lynx
#
# Example timestamp from epguides.com:
#     Sat, 14 May 2005 02:00
# Is 02:00 the time?  If so, what is the timezone?
# Is 02:00 the UTC offset?
#
# This script ignores everything after the year and assumes the time is midnight.
# In other words: it's a date, not a timestamp.

. program_guide.conf

SQL=\
"SELECT id, name, last_update, url
FROM program
WHERE do_update = 0
AND last_update IS NOT null
ORDER BY id"

DATA=`mysql -u ${MYSQLUSER} -p${MYSQLPASSWORD} -s --skip-column-names ${DATABASE} -e "${SQL}" | tr '\t' '|' | tr ' ' '_'`
if [ "$?" -ne "0" ]; then
    exit 1
fi

for i in ${DATA}
do
    ID="`echo ${i} | cut -d "|" -f 1`"
    NAME="`echo ${i} | cut -d "|" -f 2 | tr '_' ' '`"
    LAST_UPDATE="`echo ${i} | cut -d "|" -f 3`"
    URL="`echo ${i} | cut -d "|" -f 4`"

    CURRENT_TIMESTAMP="`lynx -nolist -dont_wrap_pre -dump "${URL}" | \
        egrep -e '[[:alpha:]]{3}, [[:digit:]]{2} [[:alpha:]]{3} [[:digit:]]{4} [[:digit:]]{2}:[[:digit:]]{2}' | \
        sed 's/ *//' | sed 's/ Cast Photo$//' | sed -E 's/[[:digit:]]{2}:[[:digit:]]{2}.*/00:00:00/'`"

    if [ -z "${CURRENT_TIMESTAMP}" ]; then
        echo "No date for ${NAME} (${ID})"
        continue
    fi

SQL=\
"SELECT 1       
FROM program
WHERE id = ${ID}
AND STR_TO_DATE('${CURRENT_TIMESTAMP}', '%a, %d %b %Y %k:%i') > last_update"

    RESULT=`mysql -u ${MYSQLUSER} -p${MYSQLPASSWORD} -s --skip-column-names ${DATABASE} -e "${SQL}"`
    
    if [ -z "${RESULT}" ]; then
        RESULT="0"
    fi
    
    if [ "${RESULT}" -eq "1" ]; then
        echo -n "[U] "
        mysql -u ${MYSQLUSER} -p${MYSQLPASSWORD} ${DATABASE} -e "UPDATE program SET last_update = null WHERE ID = ${ID}"
    else
        echo -n "[S] "
    fi
    
    echo "${NAME}\t\t(${LAST_UPDATE}\t${CURRENT_TIMESTAMP})"
done

exit 0

