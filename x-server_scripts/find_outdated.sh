#!/bin/sh
#
# $Id: find_outdated.sh,v 1.2 2006-01-14 22:50:12 gunter Exp $
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
. ${HOME}/.program_guide.conf

LOG=`mktemp /tmp/find_outdated.log.XXXXXX`
LOG_INSERT=`mktemp /tmp/find_outdate.insert.XXXXXX` 

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

    LAST_UPDATE_MS="`date -j -f %Y-%m-%d_%H:%M:%S "${LAST_UPDATE}" '+%s'`"

    CURRENT_TIMESTAMP="`lynx -nolist -dont_wrap_pre -dump "${URL}" | \
        egrep -e '[[:alpha:]]{3}, [[:digit:]]{2} [[:alpha:]]{3} [[:digit:]]{4} [[:digit:]]{2}:[[:digit:]]{2}' | \
        sed 's/ *//' | sed 's/ Cast Photo$//' | sed -E 's/[[:digit:]]{2}:[[:digit:]]{2}.*/00:00:00/'`"
    CURRENT_TIMESTAMP_MS="`date -j -f '%a, %d %b %Y %H:%M:%S' "${CURRENT_TIMESTAMP}" '+%s'`"

    if [ -z "${CURRENT_TIMESTAMP_MS}" ]; then
        echo "No date for ${NAME} (${ID})" >> ${LOG} 2>&1
        continue
    fi

    if [ "${LAST_UPDATE_MS}" -lt "${CURRENT_TIMESTAMP_MS}" ]; then
        echo -n "[U] " >> ${LOG} 2>&1
        mysql -u ${MYSQLUSER} -p${MYSQLPASSWORD} ${DATABASE} -e "UPDATE program SET last_update = null WHERE ID = ${ID}"
    else
        echo -n "[S] " >> ${LOG} 2>&1
    fi
    echo "${NAME}\t\t(`date -j -f '%s' ${LAST_UPDATE_MS}`\t`date -j -f '%s' ${CURRENT_TIMESTAMP_MS}`)" >> ${LOG} 2>&1
done

echo -n "INSERT INTO log VALUES (null, 'find_outdated.sh', CURRENT_TIMESTAMP(), '" > ${LOG_INSERT}
cat ${LOG} | sed "s/'/''/g" >> ${LOG_INSERT}
echo "');" >> ${LOG_INSERT}

mysql -u ${MYSQLUSER} -p${MYSQLPASSWORD} ${DATABASE} < ${LOG_INSERT}

rm -f ${LOG} ${LOG_INSERT}

exit 0

