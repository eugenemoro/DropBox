package core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import org.apache.commons.codec.digest.*;

public class UserDataBase {
	static Connection connection;

	public static void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:dropbox.db");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static void disconnect() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String getUserInfo (String userName){
		connect();
		try (PreparedStatement psGetByUserName = connection.prepareStatement("SELECT pwd FROM users WHERE userid = ?;")){
			try {
				psGetByUserName.setString(1, userName);
				String result = psGetByUserName.executeQuery().getString("pwd");
				return result;
			} catch (SQLException e) {
				return "-1";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return "-1";
	}

	public static boolean registerUser (String userName, String pwd){
		String userInfoResult = getUserInfo(userName);
		pwd = DigestUtils.sha256Hex(pwd);
		connect();
		if (!userInfoResult.equals("")){
			try (PreparedStatement insNewUser = connection.prepareStatement("INSERT INTO users(userid, pwd) VALUES(?,?);")){
				try {
					insNewUser.setString(1, userName);
					insNewUser.setString(2, pwd);
					insNewUser.executeUpdate();
					Path path = Paths.get("received/" + userName);
					if (!Files.exists(path)) {
						try {
							Files.createDirectories(path);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					return true;
				} catch (SQLException e) {
					System.out.println("Smth went wrong O_o - registerUser");
					return false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				disconnect();
			}
		} else {
			return false;
		}
		return false;
	}

	public static ArrayList<String> getListOfFiles(String userName) {
		connect();
		try (PreparedStatement psListOfFilesByUserName = connection.prepareStatement("SELECT filename FROM files WHERE userid = ?;")){
			try {
				psListOfFilesByUserName.setString(1, userName);
				ResultSet rs = psListOfFilesByUserName.executeQuery();
				ArrayList<String> listOfFiles = new ArrayList<>();
				while (rs.next()){
					listOfFiles.add(rs.getString("filename"));
				}
				return listOfFiles;
			} catch (SQLException e) {
				e.printStackTrace();
				return new ArrayList<>();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return new ArrayList<>();
	}

	public static void addFileToDB(String userName, String fileName) {
		connect();
		try (PreparedStatement insNewUser = connection.prepareStatement("INSERT INTO files(userid, filename) VALUES(?,?);")){
			insNewUser.setString(1, userName);
			insNewUser.setString(2, fileName);
			insNewUser.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Smth went wrong O_o - addFileToDB");
		} finally {
			disconnect();
		}
	}

	public static void deleteFileFromDB(String userName, String fileName) {
		connect();
		try (PreparedStatement insNewUser = connection.prepareStatement("DELETE FROM files WHERE userid = ? AND filename = ?;")){
			insNewUser.setString(1, userName);
			insNewUser.setString(2, fileName);
			insNewUser.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Smth went wrong O_o - addFileToDB");
		} finally {
			disconnect();
		}
	}
}
