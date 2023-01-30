begin=`date --date='1 day ago' +%Y-%m-%d`
end=`date +%Y-%m-%d`
SRC_USER_NAME="root"
SRC_USER_PWD="HYWWszy123."
SRC_HOST_IP="10.1.10.86"
SRC_HOST_PORT=3306
SRC_DB="wszhtj"
TAR_USER_NAME="sinosoft_rhin"
TAR_USER_PWD="sinosoft"
fields_map="\"ID\",\"BA\",\"ORGID\",\"zyh\",\"zycs\",\"dqbm\",\"dz\",\"ryrq\",\"cyrq\""
TAR_HOST_IP="10.18.103.180"
TAR_HOST_PORT="1521"
TAR_DB="orcl"
TAR_TABLENAME="BI_BA"
TAR_PRE_SQL="DELETE FROM BI_BA WHERE cyrq >= to_date('$begin', 'yyyy-mm-dd') and cyrq < to_date('$end', 'yyyy-mm-dd')"
SRC_SQL="select 
UUID() as ID, 
a.BAZA01 as ba, 
right(left(BAZA01,5),1) as orgid, 
right(BAZA01,11) as zyh, 
right(BAZA01,1) as zycs, 
a.BAZAF6 as dqbm, 
a.BAZAF7 as dz,
a.BAZA24 as ryrq,
a.BAZA27 as cyrq  
from BAZA a where a.BAZA27 >= '$begin'
and a.BAZA27 < '$end'
and a.BAZAF6 not like '4201%'" 
eval "cat <<EOF
$(< /Users/stringpeng/workspace/github/xxl-job/template/mysql2oracle.json)
EOF
"  > /Users/stringpeng/workspace/github/xxl-job/config/4.json
eval "python /usr/local/datax/bin/datax.py ./config/4.json"