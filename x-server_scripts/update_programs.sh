#!/bin/sh
#
# $Id: update_programs.sh,v 1.10 2005-12-06 05:03:00 gunter Exp $
#
. ${HOME}/.program_guide.conf

BEFORE=`mktemp /tmp/update_programs.before.XXXXXX`
AFTER=`mktemp /tmp/update_programs.after.XXXXXX`
LOG=`mktemp /tmp/update_programs.log.XXXXXX`
LOG_INSERT=`mktemp /tmp/update_programs.insert.XXXXXX` 

EPISODE_SQL=\
"SELECT *
FROM episode
ORDER BY program_id, season, number"

mysql -u ${MYSQLUSER} -p${MYSQLPASSWORD} --skip-column-names ${DATABASE} \
    -e "${EPISODE_SQL}" > ${BEFORE}

PROGRAM_SQL=\
"SELECT id, name, url
FROM program
WHERE (do_update = 1
       OR last_update is NULL)
AND url is NOT NULL"

RESULTS=`mysql -u ${MYSQLUSER} -p${MYSQLPASSWORD} -s --skip-column-names ${DATABASE} -e "${PROGRAM_SQL}" | tr '\t' '|' | tr ' ' '_'`

for result in ${RESULTS}
do
    ID="`echo ${result} | cut -d "|" -f 1`"
    NAME="`echo ${result} | cut -d "|" -f 2 | tr '_' ' '`"
    URL="`echo ${result} | cut -d "|" -f 3`"
    echo "${NAME} => ${SCRAPE} ${ID} ${NAME} ${URL}" >> ${LOG} 2>&1
    ${SCRAPE} "${ID}" "${NAME}" "${URL}" >> ${LOG} 2>&1
done

mysql -u ${MYSQLUSER} -p${MYSQLPASSWORD} --skip-column-names ${DATABASE} \
    -e "${EPISODE_SQL}" > ${AFTER}

diff -u ${BEFORE} ${AFTER} >> ${LOG} 2>&1

# Add log entry.
echo -n "INSERT INTO log VALUES (null, 'update_programs.sh', CURRENT_TIMESTAMP(), '" > ${LOG_INSERT}
cat ${LOG} | sed "s/'/''/g" >> ${LOG_INSERT}
echo "');" >> ${LOG_INSERT}

mysql -u ${MYSQLUSER} -p${MYSQLPASSWORD} ${DATABASE} < ${LOG_INSERT}

rm -f ${BEFORE} ${AFTER} ${LOG} ${LOG_INSERT}

exit 0

