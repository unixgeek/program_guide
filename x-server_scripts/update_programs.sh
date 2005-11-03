#!/bin/sh
#
# $Id: update_programs.sh,v 1.7 2005-11-03 14:35:04 gunter Exp $
#
. program_guide.config

BEFORE=`mktemp /tmp/update_programs.before.XXXXXX`
AFTER=`mktemp /tmp/update_programs.after.XXXXXX`

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
    echo "${NAME} => ${SCRAPE} ${ID} ${NAME} ${URL}"
    ./scrape.sh "${ID}" "${NAME}" "${URL}"
done

mysql -u ${MYSQLUSER} -p${MYSQLPASSWORD} --skip-column-names ${DATABASE} \
    -e "${EPISODE_SQL}" > ${AFTER}

diff -u ${BEFORE} ${AFTER}

rm -f ${BEFORE} ${AFTER}

exit 0
