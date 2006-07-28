#!/bin/sh
#
# $Id: data_audit.sh,v 1.1.2.1 2006-07-28 00:57:56 gunter Exp $
#
. program_guide.conf

OUTPUT=`mktemp /tmp/data_audit.out.XXXXXX`

mysql -t -u ${MYSQLUSER} -p${MYSQLPASSWORD} ${DATABASE} < ../database/data_quality_reports.sql > ${OUTPUT} 

COUNT=`wc -l ${OUTPUT} | tr -s ' ' | cut -d " "  -f 2`

if [ "${COUNT}" -eq "0" ]; then
    exit 0
fi

cat ${OUTPUT}
