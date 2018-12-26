/**     
 * @author js   
 * @date 2018年5月12日   
 * @version 1.0   
 */ 
package com.wisdom.wy.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class BaseConnection {

	public static Connection getConnection() {
		Connection conn = null;
		String url = "";
		String userName = "";
		String passWord = "";
		try{  
            Class.forName("com.mysql.jdbc.Driver");//加载驱动类  
            url = "*";
//            url = "jdbc:mysql://192.168.0.3:3306/energydb";
//            url = "jdbc:mysql://localhost:3306/energydb";
            //url = "jdbc:oracle:thin:@localhost:1521:orcl";
            userName = "*";
//            userName = "root";
            passWord = "*";
            conn = DriverManager.     
                    getConnection(url,userName,passWord);  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
        return conn;  
	}
}
