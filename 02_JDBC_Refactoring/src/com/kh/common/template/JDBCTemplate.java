package com.kh.common.template;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;

public class JDBCTemplate {
	/*
	 * Refactoring 
	 *   - 기존의 코드를 변경하여 코드를 더 이해하기 쉽고 유지보수하기 쉽게 만드는 과정. 
	 *   - 코드중복제거, 공용메서드, 클래스 추출, 변수명 변경 등등, 코드의 가독성과 유지보수성을 향상시키는게 목적
	 *   -  
	 */
	
	/*
	 * 커넥션 객체를 생성(DriverManager.getConnection) 및 종료(close)하는 작업은 처리시간이 많이 들고 자원을 많이
	 * 소모하는 고자원이다. 따라서 그때그때 커넥션을 생성 및 종료하는 행위는 효율적이지 못함.
	 * 
	 * 객체를 생성 및 소멸하는 것이 문제이므로 객체를 "미리" 생성해두고 필요할때마다 꺼내 쓰면서 종료시키는게 아니라 사용한곳으로 자원을
	 * "반납" 하면된다.
	 * 
	 * DataSource - dbms와의 연결, 커넥션풀 관리 및 생성 커넥션풀의 커넥션을 활용한 기능들을 정의하기 위한 "인터페이스" - 대표
	 * DataSource 인터페이스 구현 클래스 : DBCP, HikariCP, Tomcat DataSource - dbcp 라이브러리 다운로드
	 * 방법. 
	 * 1) 메이븐레파지토리 접속 
	 * 2) Apache commons dbcp 검색 -> 2.9 버전 다운로드 
	 * 3) dependencies library 확인후 함계 다운로드받아야 되는 jar들 모두 다운로드 - apache commons pool2, apache commons logging 
	 * 4) dev로 이동시킨후 현재 프로젝트에 추가.
	 * 
	 * BasicDataSource?
	 *  - javax.sql.DataSource인터페이스를 구현한 클래스 
	 *  - 데이터베이스에 연결 및 커넥션 풀 생성. 커넥션 생성 및 소멸등 커넥션 관련된 예외처리등을 효율적으로 다루는 메소드를 제공.
	 *  
	 */

	// static 변수, 블록으로 설정해준다.
	static String driverClass;
	static String url;
	static String username;
	static String password;
	
	// 일반블럭 : 메서드 호출, 변수의 생성을 할때 쓴다. 
	// static 초기화 블럭 : 객체가 생성될때가 아니라 프로그램이 실행될때, 동시에 실행된다. 
	// static은 처음 한번만 실행되기 때문에, 함께 이 코드를 실행한다. 
	
	static {
		Properties prop = new Properties();
		String filename = "resources/driver.properties";
		
		try {
			prop.load(new FileReader(filename));
			
			driverClass = prop.getProperty("driver");
			url = prop.getProperty("url");
			username = prop.getProperty("username");
			password = prop.getProperty("password");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public static Connection getConnection() {
		
		
		
		Connection conn = null;
		BasicDataSource dataSource = new BasicDataSource();
		try {

			dataSource.setDriverClassName(driverClass); // 데이터베이스 드라이버 등록
			dataSource.setUrl(url); // url 주소 설정
			dataSource.setUsername(username); // 계정 이름
			dataSource.setPassword(password); // 계정 비밀번호
			dataSource.setDefaultAutoCommit(false); // 커넥션 생성시 자동커밋여부
			dataSource.setRemoveAbandonedTimeout(60); // 사용하고 있지 않는 커넥션 삭제 (60분동안 사용안하면 삭제)
			dataSource.setInitialSize(10); // 초기 커넥션풀 사이즈지정
			dataSource.setMaxTotal(30); // 커넥션풀이 가질 수 있는 최대 커넥션 수 지정
			dataSource.setMaxWaitMillis(1000); // 커넥션풀에 커넥션이 없을때 대기할 최대 시간 지정 (1분) -1이면 무한대기

			conn = dataSource.getConnection(); // dataSource 에 존재하는 커넥션 가져오기
			// conn.setAutoCommit(false);
			
			/*
			 *  커넥션풀의 장점 
			 *  
			 *  - 자원 관리 : 사용하고 닫아두지 않은 커넥션들을 일정시간이 지났을때 자동으로 삭제해주는 기능이 내장되어 있음. 
			 *  - 성능향상 : 커넥션풀은 데이터베이스 연결을 미리 생성하고 커넥션풀에 유지시킴으로써 애플리케이션 성능을 향상
			 *  		   시킬수 있다.(생성시의 비용해결)
			 *  - 확장성 : 커넥션풀에서 만들어 놓은 다양한 옵션을 통해 원하는 커넥션풀을 생성할 수 있음.
			 *  
			 *  커넥션풀의 단점
			 *   
			 *  - 유지자체만으로 자원을 소비한다. 
			 *    많은 커넥션을 만들어두면 자원을 그만큼 차지하게 되서 성능이 저하된다.
			 *      
			 *  - 커넥션을 생성해둔다는 것은 그 갯수만큼 항상 DBMS서버와 연결되어 있는 상태에 있는거고, 연결상태에 있으면 
			 *    연결을 유지하기 위한 메모리를 할당받음. 메모리가 많으면 DB는 적절한 SQL실행계획을 짜지 못할 수 있음.
			 *  
			 *  - 커넥션풀이 너무 많은 커넥션이 존재하면 사용되지 않는 커넥션이 많아 질 수 있다.
			 *    
			 *  - 따라서 커넥션은 동시사용자수, 트래픽을 잘 모니터링하여 적절한 수를 유지하는것이 중요함.
			 * 
			 */
			
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
