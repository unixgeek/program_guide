#!/bin/sh
#
# $Id: update_programs.sh,v 1.4 2005-10-27 03:15:49 gunter Exp $
#
. program_guide.config

SQL=\
"SELECT id, name, url
FROM program
WHERE (do_update = 1
       OR last_update is NULL)
AND url is NOT NULL"

RESULTS=`mysql -u ${MYSQLUSER} -s --skip-column-names ${DATABASE} -e "${SQL}" | tr '\t' '|' | tr ' ' '_'`

for result in ${RESULTS}
do
    ID="`echo ${result} | cut -d "|" -f 1`"
    NAME="`echo ${result} | cut -d "|" -f 2 | tr '_' ' '`"
    URL="`echo ${result} | cut -d "|" -f 3`"
    echo "${NAME} => ./scrape ${ID} ${NAME} ${URL}"
    ./scrape.sh "${ID}" "${NAME}" "${URL}"
done

exit 0
