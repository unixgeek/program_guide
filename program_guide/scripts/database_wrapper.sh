#!/bin/sh
#
# $Id: database_wrapper.sh,v 1.1.6.2 2006-08-31 04:24:34 gunter Exp $
#
# Set max_allowed_packet to at least the size of the content
# column.  The content column is a mediumblob so the size is
# 16777215.
# Requires FILE privilege.
. program_guide.conf

if [ "$#" -lt "1" ]; then
    echo "`basename $0` SCRIPT [ARGS] [...]"
    exit 1
fi

LOG=`mktemp /tmp/database_wrapper.log.XXXXXX`
# Set permission so the mysqld process can read it.
chmod 655 ${LOG}

SCRIPT=$1
shift
${SCRIPT} "$*" 2>&1 | gzip -c -9 > ${LOG}

COUNT=`gunzip -c ${LOG} | wc -c | tr -d ' '`
if [ "${COUNT}" -eq "0" ]; then
    rm -f ${LOG}
    exit 0
fi

# Add log entry.
SQL=\
"INSERT INTO log VALUES (
    null, 
    '`basename ${SCRIPT}`',
    CURRENT_TIMESTAMP(),
    'PENDING UPDATE');

UPDATE log
SET content = LOAD_FILE('${LOG}')
WHERE id = LAST_INSERT_ID();"

mysql --max_allowed_packet=16777216 -u ${MYSQLUSER} -p${MYSQLPASSWORD} \
    ${DATABASE} -e "${SQL}"

rm -f ${LOG}

exit 0

