#!/bin/sh
#
# $Id: update_programs.sh,v 1.1.2.1 2006-05-09 02:19:15 gunter Exp $
#
. program_guide.conf

PROGRAM_SQL=\
"SELECT id, name, url
FROM program
WHERE (do_update = 1
       OR last_update is NULL)
AND url is NOT NULL"

RESULTS=`mysql -u ${MYSQLUSER} -p${MYSQLPASSWORD} -s --skip-column-names ${DATABASE} -e "${PROGRAM_SQL}" | tr '\t' '|' | tr ' ' '_'`

if [ -z "${RESULTS}" ]; then
    echo "Nothing to do :(."
    exit 0
fi

for result in ${RESULTS}
do
    ID="`echo ${result} | cut -d "|" -f 1`"
    NAME="`echo ${result} | cut -d "|" -f 2 | tr '_' ' '`"
    URL="`echo ${result} | cut -d "|" -f 3`"
    echo "${NAME} => ${SCRAPE} ${ID}"
    ${SCRAPE} "${ID}"
done

exit 0

