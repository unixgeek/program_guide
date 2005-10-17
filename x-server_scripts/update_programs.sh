#!/bin/sh
#
# $Id: update_programs.sh,v 1.1 2005-10-17 05:25:55 gunter Exp $
#
. program_guide.config

SQL=\
"SELECT id, name, url
FROM program
WHERE do_update = 1"

RESULTS=`mysql -s --skip-column-names ${DATABASE} -e "${SQL}" | tr '\t' '|' | tr ' ' '_'`

for result in ${RESULTS}
do
    ID="`echo ${result} | cut -d "|" -f 1`"
    NAME="`echo ${result} | cut -d "|" -f 2 | tr '_' ' '`"
    URL="`echo ${result} | cut -d "|" -f 3`"
    echo "${NAME} => ./scrape ${ID} ${URL}"
    ./scrape.sh "${ID}" "${URL}"
done

exit 0
