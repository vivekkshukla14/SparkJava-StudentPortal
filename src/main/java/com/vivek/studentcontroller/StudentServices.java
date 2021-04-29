package com.vivek.studentcontroller;

import com.google.gson.Gson;
import com.vivek.bean.StudentBean;
import com.vivek.dao.StudentDao;

import spark.*;

public class StudentServices {

	static Gson gson = new Gson();

	public static final Route addStudent = (req, res) -> {

		try {

			String body = req.body();
			StudentBean studbean = gson.fromJson(body, StudentBean.class);

			String studid = studbean.getId();
			String studname = studbean.getName();
			String studdob = studbean.getDob();

			if (studid != null && studid != "" && studname != "") {
				return StudentDao.addStudentDB(studid, studname, studdob);
			}
		} catch (Exception exp) {
			exp.printStackTrace();
			return "Error Occured while adding student to database";

		}
		return "Check your Entered Values";

	};

	public static final Route findStudent = (req, res) -> {

		try {
			String studid = req.params(":id");
			StudentBean studbean = StudentDao.findStudentDB(studid);

			String beanid = studbean.getId();
			String beanname = studbean.getName();
			String beandob = studbean.getDob();
			String beanmsg = studbean.getMsg();
			if (studid != null && studid != "") {
				if (beanid.equals(studid)) {
					return ("[ Student ID: " + beanid + " ] [ Student Name: " + beanname + "] [ Student DOB: " + beandob)+" ]";
				} else {
					return beanmsg;
				}
			} else {
				return beanmsg;
			}
		} catch (Exception e) {
			System.out.println(e);
			return "Some error occured while fetching";
		}
	};

	public static final Route updateStudent = (req, res) -> {

		try {

			String body = req.body();
			StudentBean studbean = gson.fromJson(body, StudentBean.class);

			String studid = req.params(":id");
			String studname = studbean.getName();
			String studdob = studbean.getDob();
			//System.out.println(studid);
			if (studid != null && studid != "" && studname != "") {
				return StudentDao.updateStudentDB(studid, studname, studdob);
			} else if (studid == null) {
				return "Student id cant be null";
			} else {
				return "Something is Wrong";
			}

		} catch (Exception e) {
			System.out.println(e);
			return "Some error occured while Updating";
		}
		// return "Something is Wrong";
	};

	public static final Route deleteStudent = (req, res) -> {

		try {
			String studid = req.params(":id");
			if (studid != null && studid != "") {
				return StudentDao.deleteStudentDB(studid);
			} else {
				return "Check Your Id";
			}
		} catch (Exception err) {
			return "Some Error Occured while deletion";
		}

	};

	public static final Route listStudent = (req, res) -> {

		try {

			return StudentDao.listStudentDB();

		} catch (Exception ex) {
			System.out.println(ex);
			return "Error Exception ";
		}

	};

}
