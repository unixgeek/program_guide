#!/bin/sh
#
# $Id: load_episodes.sh,v 1.1 2006-07-16 18:07:52 gunter Exp $
#
#
. program_guide.conf
if [ "$#" -ne "1" ]; then
    echo "usage: `basename $0` FILE"
    exit 1
fi

FILE="$1"

LOAD=\
"LOAD DATA LOCAL INFILE '${FILE}' REPLACE
INTO TABLE episode FIELDS TERMINATED BY '|'
(program_id, season, number, production_code, original_air_date, title, serial_number, summary_url)"
mysql --local-infile=1 -u ${MYSQLUSER} -p${MYSQLPASSWORD} ${DATABASE} -e "${LOAD}"
if [ "$?" -ne "0" ]; then
    exit 1
fi 

exit 0

