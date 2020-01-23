package com.soar.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.soar.commons.Constant;

public class DatabaseOperation {

	public Map<Integer, String[]> getUrlsFormDatabase() throws SQLException {
		Map<Integer, String[]> urls = new HashMap<Integer, String[]>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			/*
			 * Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); con =
			 * DriverManager.getConnection("jdbc:sqlserver://" + Constant.getDbHost() + ":"
			 * + Constant.getDbPort() + ";user=" + Constant.getDbUsername() + ";password=" +
			 * Constant.getDbPassword() + ";database=" + Constant.getDbName());
			 */
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/soar", Constant.getDbUsername(),
					Constant.getDbPassword());
			stmt = con.createStatement();
			rs = stmt.executeQuery("select * from soar_ip_list");
			int i = 1;
			while (rs.next()) {
				urls.put(i++, new String[] { rs.getString(2), rs.getString(3) });
			}

		} catch (Exception ex) {
			System.err.println(ex);

		} finally {
			con.close();

		}
		return urls;
	}

	public static void main(String[] args) throws SQLException {
		DatabaseOperation con = new DatabaseOperation();
		Map<Integer, String[]> urls = con.getUrlsFormDatabase();

		for (int i = 0; i < urls.size(); i++) {
			String[] single = urls.get(i + 1);
			System.out.println(single[0] +" " +  single[1] );
		}

	}

}
