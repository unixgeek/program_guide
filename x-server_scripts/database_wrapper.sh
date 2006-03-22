#!/bin/sh
#
# $Id: database_wrapper.sh,v 1.1 2006-03-22 04:57:20 gunter Exp $
#
. program_guide.conf

LOG=`mktemp /tmp/database_wrapper.log.XXXXXX`
LOG_INSERT=`mktemp /tmp/database_wrapper.insert.XXXXXX` 
if [ "$#" -lt "1" ]; then
    echo "`basename $0` SCRIPT [ARGS] [...]"
    exit 1
fi

SCRIPT=$1
shift
${SCRIPT} "$*" > ${LOG} 2>&1

# Add log entry.
echo -n "INSERT INTO log VALUES (null, '`basename ${SCRIPT}`', CURRENT_TIMESTAMP(), '" > ${LOG_INSERT}
cat ${LOG} | sed "s/'/''/g" >> ${LOG_INSERT}
echo "');" >> ${LOG_INSERT}

mysql -u ${MYSQLUSER} -p${MYSQLPASSWORD} ${DATABASE} < ${LOG_INSERT}

rm -f ${LOG} ${LOG_INSERT}

exit 0

