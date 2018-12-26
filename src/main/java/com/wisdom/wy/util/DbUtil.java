/**
 * @Title:DbUtil.java
 * @author:js
 */
package com.wisdom.wy.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author js
 *
 */
public class DbUtil {
	public List<Map<String, Object>> queryForList(String consNo) {
		String sql = new SqlUtil().queryById(consNo);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Connection connection = BaseConnection.getConnection();
		PreparedStatement pStatement = null;
		ResultSet resultSet = null;
		try {
			pStatement = connection.prepareStatement(sql);

			resultSet = pStatement.executeQuery();
			ResultSetMetaData md = resultSet.getMetaData();
			int count = md.getColumnCount();
			while (resultSet.next()) {
				Map<String, Object> rowData = new HashMap<String, Object>();
				for (int i = 1; i <= count; i++) {
					rowData.put(md.getColumnName(i), resultSet.getObject(i));
				}
				list.add(rowData);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if (null != resultSet) {
					resultSet.close();
				}
				if (null != pStatement) {
					pStatement.close();
				}
				if (null != connection) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public boolean executeSql(String consNo, String factMoney) {
		String sql = new SqlUtil().updateSql(consNo,factMoney);
		Connection connection = BaseConnection.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.execute();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public void recordSql(String[] msg,String consId) {
		String sql = new SqlUtil().insertSql(msg,consId);
		Connection connection = BaseConnection.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void deleteSql(String[] msg) {
		String sql = new SqlUtil().delSql(msg);
		Connection connection = BaseConnection.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
