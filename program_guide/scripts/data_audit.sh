#!/bin/sh
#
# $Id: data_audit.sh,v 1.1.4.1 2006-08-16 00:18:58 gunter Exp $
#
. program_guide.conf

OUTPUT=`mktemp /tmp/data_audit.out.XXXXXX`

mysql -t -u ${MYSQLUSER} -p${MYSQLPASSWORD} ${DATABASE} < ../database/data_quality_reports.sql > ${OUTPUT} 

COUNT=`wc -l ${OUTPUT} | tr -s ' ' | cut -d " "  -f 2`

if [ "${COUNT}" -eq "0" ]; then
    exit 0
fi

cat ${OUTPUT}

rm -f ${OUTPUT}
