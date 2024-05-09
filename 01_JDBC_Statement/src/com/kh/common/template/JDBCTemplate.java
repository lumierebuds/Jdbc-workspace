package com.kh.common.template;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCTemplate {
	/*
	 * Template? - JDBC과정중 반복적으로 쓰이는 구문들을 각각의 메소드로 정의한 클래스. -
	 * 
	 */

	// 공통되는 소스코드
	// 1) DB와 연결하여 Connection 객체를 반환하는 부분.

	public static Connection getConnection() {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "C##JDBC", "JDBC");
			conn.setAutoCommit(false);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;
	}

	// 2) 전달받은 jdbc용 객체를 대신 반납시켜주는 메소드 (자원반납)
	// 2_1) Connection 객체를 반납시켜주는 메서드

	public static void close(Connection conn) {

		try {
			if (conn != null && !conn.isClosed())
				// conn이 null이 아닐때 닫아야한다. null이면 NullPointerException 발생
				// conn이 닫히지 않을때 닫아야한다.
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 2_2) Statement 객체를 반납시켜주는 메서드(오버로딩)

	public static void close(Statement stmt) {
		try {
			if (stmt != null && !stmt.isClosed())
				stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 2_3) ResultSet객체를 반납시켜주는 메소드
	public static void close(ResultSet rset) {
		try {
			if (rset != null && !rset.isClosed())
				rset.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 2_4) 통합해서 반납시켜주는 메서드
	public static void close(Connection conn, Statement stmt, ResultSet rset) {
		close(rset);
		close(stmt);
		close(conn); // 위에 반납시켜주는 메서드를 순서대로 처리
	}

	// 3. 전달받은 Connection을 활용해서 commit, rollback 메서드
	// 3_1) 전달받은 Connection을 가지고 COMMIT시켜주는 메서드
	public static void commit(Connection conn) {

		try {
			if (conn != null && !conn.isClosed())
				conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 3_2) 전달받은 Connection 가지고 rollback 시켜주는 메서드
	public static void rollback(Connection conn) {
		try {
			if (conn != null && !conn.isClosed())
				conn.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
