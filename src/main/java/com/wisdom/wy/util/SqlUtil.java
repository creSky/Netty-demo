/**     
 * @author js   
 * @date 2018年12月14日   
 * @version 1.0   
 */ 
package com.wisdom.wy.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SqlUtil {
	private static SimpleDateFormat sdFormat=new SimpleDateFormat("yyyyMM");
	private static String comma = ",";
	public String queryById(String consNo) {
		String sql = "";
		sql = "select c.CONS_NAME,c.CONS_ID,c.TRIP_LIMIT,s.BALANCE from c_cons c,sf_pre_charge s"
				+ " where c.CONS_ID=s.CONS_ID and c.CONS_NO=" + consNo;
		return sql;
	}
	
	public String updateSql(String consNo, String factMoney) {
		String sql = "";
		sql = "update sf_pre_charge t set t.BALANCE=t.BALANCE+" + factMoney + ",t.MODIFY_DATE=NOW() where t.CONS_ID=("
				+ "select c.CONS_ID from C_CONS c where t.CONS_ID=c.CONS_ID and c.CONS_NO=" + consNo + ")";
		System.out.println(sql);
		return sql;
	}
	public String insertSql(String[] msg,String consId) {
		String sql = "";
		String mon = sdFormat.format(new Date());
		sql = "INSERT INTO SF_CHARGE_RECORD (YS_WARRANT_NO,ID_FRAGMENT,PAY_DATE,USER_NO,MON,MON_SN," +
				"PAID_FLAG,GROUP_NO,YS_TYPE_CODE,FACT_MONEY,F_CHARGE_MODE,COLLECTOR_ID) ";
		String valueSql = " VALUES ("+msg[2]+",1,now(),"+consId+comma+mon+comma+"1"+comma+"1"+comma+"0"
						+ comma+"1"+comma+msg[5]+ comma+"02"+comma+"'bank'"+")";
		sql += valueSql;
		System.out.println(sql);
		return sql;
	}
	
	public String delSql(String[] msg) {
		String sql = "";
		sql = "DELETE FROM sf_charge_record WHERE YS_WARRANT_NO=" + msg[5];
		System.out.println(sql);
		return sql;
	}
}
