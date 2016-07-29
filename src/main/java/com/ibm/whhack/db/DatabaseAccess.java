package com.ibm.whhack.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseAccess {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://169.44.118.61/enterpriseregistrydb";

	static final String USER = "root";
	static final String PASS = "rootpass";

	static Connection conn = null;
	static PreparedStatement searchStmt = null, addStmt = null;
	
	static {
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			searchStmt = 	conn.prepareStatement("SELECT * FROM `enterpriseregistrydb`.`users`"
												+ "WHERE `username` = ?");
			addStmt = 	conn.prepareStatement("INSERT INTO `enterpriseregistrydb`.`users`"
											+ "(`username`, `password`, `authorized_user`)"
											+ "VALUES"
											+ "(?, ?, ?)");
			
			String createUsersTable = "CREATE TABLE IF NOT EXISTS `enterpriseregistrydb`.`users` ("
									+ "`username` VARCHAR(45) NOT NULL,"
									+ "`password` VARCHAR(45) NOT NULL,"
									+ "`authorized_user` BIT(1) NOT NULL,"
									+ "PRIMARY KEY (`username`))";
			
			conn.createStatement().execute(createUsersTable);
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createUser(String username, String password, boolean authorizedUser) throws SQLException {
		addStmt.setString(1, username.toLowerCase());
		addStmt.setString(2, password);
		addStmt.setBoolean(3, authorizedUser);
		addStmt.executeUpdate();
	}
	
	public static boolean userValid(String username, String password) throws SQLException {
		username = username.toLowerCase();
		searchStmt.setString(1, username);
		ResultSet rs = searchStmt.executeQuery();
		while (rs.next()) {
			String passwdFromDb = rs.getString(2);
			if (passwdFromDb.equals(password))
				return true;
		}
		return false;
	}
	
	public static boolean userAuthorized(String username) throws SQLException {
		username = username.toLowerCase();
		searchStmt.setString(1, username);
		ResultSet rs = searchStmt.executeQuery();
		while (rs.next()) {
			return rs.getBoolean(3);
		}
		return false;
	}
}
