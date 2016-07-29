package com.ibm.whhack.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
	static PreparedStatement searchUsersStmt = null, addUserStmt = null, searchRequestsStmt = null, addRequestStmt = null;
	
	static {
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			
			searchUsersStmt = 	conn.prepareStatement("SELECT * FROM `enterpriseregistrydb`.`users`"
												+ "WHERE `username` = ?");
			addUserStmt = 	conn.prepareStatement("INSERT INTO `enterpriseregistrydb`.`users`"
											+ "(`username`, `password`, `authorized_user`)"
											+ "VALUES"
											+ "(?, ?, ?)");
			
			searchRequestsStmt = 	conn.prepareStatement("SELECT * FROM `enterpriseregistrydb`.`requests`"
														+ "WHERE `username` = ?");
			addRequestStmt = 	conn.prepareStatement("INSERT INTO `enterpriseregistrydb`.`requests`"
											+ "(`username`, `image_stream`, "
											+ "`classifier_id`,`request_timestamp`)"
											+ "VALUES"
											+ "(?, ?, ?, now())");
			
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
		addUserStmt.setString(1, username.toLowerCase());
		addUserStmt.setString(2, password);
		addUserStmt.setBoolean(3, authorizedUser);
		addUserStmt.executeUpdate();
	}
	
	public static boolean userValid(String username, String password) throws SQLException {
		username = username.toLowerCase();
		searchUsersStmt.setString(1, username);
		ResultSet rs = searchUsersStmt.executeQuery();
		if (rs.next()) {
			String passwdFromDb = rs.getString(2);
			if (passwdFromDb.equals(password))
				return true;
		}
		return false;
	}
	
	public static boolean userAuthorized(String username) throws SQLException {
		username = username.toLowerCase();
		searchUsersStmt.setString(1, username);
		ResultSet rs = searchUsersStmt.executeQuery();
		if (rs.next()) {
			return rs.getBoolean(3);
		}
		return false;
	}
	
	public static void insertRequest(String username, String classid, File file) throws SQLException, FileNotFoundException, IllegalAccessException {
		if (!userAuthorized(username))
			throw new IllegalAccessException("User is not authorized to insert requests.");
		
		FileInputStream io = new FileInputStream(file);
		addRequestStmt.setString(1, username.toLowerCase());
		addRequestStmt.setBinaryStream(2, io , (int)file.length());
		addRequestStmt.setString(3, classid);
		addRequestStmt.executeUpdate();
	}
	
	public static InputStream retrieve(String username) throws SQLException {
		username = username.toLowerCase();
		searchRequestsStmt.setString(1, username);
		ResultSet rs = searchRequestsStmt.executeQuery();
		if (rs.next()) {
			return rs.getBinaryStream(2);
		}
		return null;
	}
}
