package com.kh.model.vo;

import java.sql.Date;

/*
 * VO(Value Object)
 * - 어플리케이션을 이루는데 있어서 "핵심적인" 역할을 하는 클래스를 정의하는 클래스. 
 * - 필요에 의해 hashCode, equals, 그외 함수를 정의할 수 있음. 
 * - DB테이블의 정보를 기록하는 용도의 객체 
 * 
 * DTO(Data Transfer Object) 
 * - 데이터 전송용 객체 
 * - API 서버간의 데이터 송수신시에만 사용된다. 
 * 
 */
public class Member {

	private int userNo; 		// USER_NO NUMBER
	private String userId; 		// USER_ID VARCHAR2(30 BYTE)
	private String userPwd; 	// USER_PWD VARCHAR2(100 BYTE)
	private String userName;	// USER_NAME VARCHAR2(15 BYTE)
	private String email; 		// EMAIL VARCHAR2(100 BYTE)
	private String birthday; 	// BIRTHDAY VARCHAR2(6 BYTE)
	private char gender; 		// GENDER VARCHAR2(1 BYTE)
	private String phone; 		// PHONE VARCHAR2(13 BYTE)
	private String address; 	// ADDRESS VARCHAR2(100 BYTE)
	private Date enrollDate; 	// ENROLL_DATE DATE -- DATE는 'java.sql.date'를 import 해야한다.
	private Date modifyDate; 	// MODIFY_DATE DATE
	private char status; 		// STATUS VARCHAR2(1 BYTE)

	public Member() {

	}

	public Member(int userNo, String userId, String userPwd, String userName, String email, String birthday,
			char gender, String phone, String address, Date enrollDate, Date modifyDate, char status) {
		this.userNo = userNo;
		this.userId = userId;
		this.userPwd = userPwd;
		this.userName = userName;
		this.email = email;
		this.birthday = birthday;
		this.gender = gender;
		this.phone = phone;
		this.address = address;
		this.enrollDate = enrollDate;
		this.modifyDate = modifyDate;
		this.status = status;
	}

	public int getUserNo() {
		return userNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getEnrollDate() {
		return enrollDate;
	}

	public void setEnrollDate(Date enrollDate) {
		this.enrollDate = enrollDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Member [userNo=" + userNo + ", userId=" + userId + ", userPwd=" + userPwd + ", userName=" + userName
				+ ", email=" + email + ", birthday=" + birthday + ", gender=" + gender + ", phone=" + phone
				+ ", address=" + address + ", enrollDate=" + enrollDate + ", modifyDate=" + modifyDate + ", status="
				+ status + "]";
	}
	
}
