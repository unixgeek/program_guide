#!/bin/sh
#
# $Id: data_audit.sh,v 1.2 2006-08-15 18:14:34 gunter Exp $
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
