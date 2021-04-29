package com.vivek.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.vivek.bean.StudentBean;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class StudentDao {

	private static Connection con = null;
	private static PreparedStatement stat = null;
	private static ResultSet set = null;

	public StudentDao() {
		String fileName = "config.properties";
		ClassLoader classLoader = StudentDao.class.getClassLoader();
		try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {

			Properties prop = new Properties();
			prop.load(inputStream);
			String connectionUrl = prop.getProperty("db.url");
			String username = prop.getProperty("db.user");
			String password = prop.getProperty("db.password");
			
			con = DriverManager.getConnection(connectionUrl, username, password);
			if (con != null) {
				System.out.println("Connection Successful");
			} else {
				System.out.println("Connection Failed");
			}
		} catch (Exception e) {
			System.out.println("Some Error Occured");
			e.printStackTrace();
		}
	}

	public static List<StudentBean> listStudentDB() {
		try {
			Map<String, StudentBean> studmap = new HashMap<>();
			stat = con.prepareStatement("select * from student");
			set = stat.executeQuery();

			while (set.next()) {
				StudentBean studbean = new StudentBean();

				studbean.setId(set.getString("id"));
				studbean.setName(set.getString("name"));
				studbean.setDob(set.getString("dob"));
				// System.out.println(studbean.getDob());
				studmap.put(studbean.getId(), studbean);
			}
			return new ArrayList<>(studmap.values());
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public static String addStudentDB(String studid, String studname, String studdob) {
		try {
			if (con != null && studid != null) {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = sdf.parse(studdob);
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				stat = con.prepareStatement("INSERT INTO student (id,name,dob) VALUES (?,?,?)");
				stat.setString(1, studid);
				stat.setString(2, studname);
				stat.setDate(3, sqlDate);
				int i = stat.executeUpdate();
				if (i > 0) {
					return "Student Added Succesfully";
				} else {
					return "Student Not Added";
				}
			} else if (con == null) {
				return "Connection not Established";
			} else if (studid == null) {
				return "Id is Empty";
			} else {
				return "Check your Entered Values";
			}
		} catch (Exception e) {
			System.out.println(e);
			return e.getMessage();
		}

	}

	public static StudentBean findStudentDB(String studid) {

		StudentBean studbean = new StudentBean();
		try {
			if (studid != null && con != null) {
				stat = con.prepareStatement("select * from student where id=?");
				stat.setString(1, studid);
				set = stat.executeQuery();
				if (set.next()) {
					studbean.setId(set.getString("id"));
					studbean.setName(set.getString("name"));
					studbean.setDob(set.getString("dob"));
					return studbean;
				} else {
					studbean.setMsg("Student not Found");
					return studbean;
				}
			} else if (con == null) {
				studbean.setMsg("Connection not Established");
				return studbean;
			} else {
				studbean.setMsg("Check Input");
				return studbean;
			}
		} catch (Exception e) {
			studbean.setMsg("Error Occured while Fetching Student Id,Check your id or try again later");
			return studbean;
		}

	}

	public static String updateStudentDB(String studid, String studname, String studdob) {
		try {
			SimpleDateFormat utilDate = new SimpleDateFormat("yyyy-MM-dd");
			Date date = utilDate.parse(studdob);
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());

			if (studid != null && studname != null) {
				stat = con.prepareStatement("update student set name=?, dob=? where id=?");
				stat.setString(1, studname);
				stat.setDate(2, sqlDate);
				stat.setString(3, studid);
				int i = stat.executeUpdate();
				if (i > 0) {
					return "Student data updated successfully";
				} else {
					return "Updation Failed ,maybe Student not present in the data";
				}
			} else if (con == null) {
				return "Connection not Established";
			} else {

				return "Input Error";
			}
		} catch (Exception e) {
			System.out.println(e);
			return "Error Occured while Fetching or Updating Data,plz try again later";
		}
	}

	public static String deleteStudentDB(String studid) {

		try {
			if (con != null) {
				int i = 0;
				if (studid != null) {
					stat = con.prepareStatement("delete from student where id=?");
					stat.setString(1, studid);
					i = stat.executeUpdate();
					if (i > 0) {
						return "RECORD DELETED SUCCESFULLY";
					} else {
						return "Record Deletion Failed";
					}
				}
			} else {
				return "Connection not Established";
			}
		} catch (Exception e) {
			return "Check your Input";

		}
		return studid;
	}

}