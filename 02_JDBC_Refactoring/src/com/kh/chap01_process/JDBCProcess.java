package com.kh.chap01_process;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
	
	
	public class JDBCProcess {
		/*
		 * 
		 * JDBC (Java DataBase Connectivity) 
		 * 
		 * - Java 애플리케이션과 데이터베이스(RDBMS)간의 연동시 필요한 연결방법, SQL문을 전달하고 결과값을 돌려받는 
		 * 	 방법들을 정의해둔 자바의 API. 
		 * - JDBC는 연결하고자 하는 데이터베이스가 무엇이던 일관된 방법으로 통신할 수 있게 표준화 시켰음.
		 * 
		 * 
		 * JDBC를 배워야 하는 이유? 
		 * - Spring, MyBatis, JPA등에서 JDBC를 활용했기 때문에 작동방식을 이해하기 위해서는 JDBC를 알아야함. 
		 * 
		 * JDBC 주요 객체들 
		 * - RDBMS들과 "연결"하기위한 객체, 실행할 SQL문을 RDBMS에 "전달"하는 객체, 결과값을 "반환"받는 객체 등이 있음. 
		 * 
		 *  1) XXXDriver : db와 연결을 담당하는 핵심 클래스. 각 밴더(회사)에서 java.sql.Driver인터페이스를 구현한
		 *     클래스. -> 자바에서 기본적으로 제공하지 않기 때문에 다운로드 필요.
		 *  
		 *  2) DriverManger : Driver들을 관리하는 클래스. 여러개의 driver들중 어떤 driver를 통해 db와 연결할지 
		 *  				  선택할 수 있다.
		 *  
		 *   3) Connection : db와 연결된 상태임을 나타내는 객체. dbms와 연결 설정 및 해제. sql문 전달도 가능.
		 *   				 트랜잭션관리 가능. Statement 생성가능
		 * 	
		 * 	 4) Statement : 연결된 db에 sql문을 전달하고 실행한 후 결과값을 받아내는 객체. 
		 * 
		 *	 5) ResultSet : 실행한 sql문이 select문일 경우 조회 결과들을 받아낼 수 있는 객체.
		 */
		
		public static void main(String[] args) {
			/*
			 * JDBC API 코딩 흐름 
			 * 1) Driver 등록 : 연결하고자 하는 RDBMS사에서 제공하는 Driver 클래스 등록 
			 * 2) DBMS 연결 : 접속하고자 하는 DB정보(URL, ID, PASS)를 입력하여 DB에 접속 
			 * 3) STATEMENT 생성 : DB 연결시 생성된 Connection 객체를 통해 Statement 객체 생성 
			 * 4) SQL문 실행 : Statement 객체를 통해서 sql문 실행
			 * 5) 결과값 반환 : 수행한 sql문이 select인 경우 ResultSet 객체, dml문일경우 int자료형 값 반환 
			 * 6) 트랜잭션처리 / ResultSet 내부의 데이터들을 알맞은 vo객체로 변환 
			 */
			
				
			
			/*
			 * 1) 오라클 Driver 등록 -- 사이트에서 다운받아도 되고, 설치된 Oracle 데이터베이스에서 확인할 수 있음
			 *  * 오라클 드라이버 등록방법 
			 *  1. 프로젝트 우클릭 후 properties 선택
			 *  2. java build path로 이동
			 *  3. Libraries로 이동후 module path 선택
			 *  4. 우측 add external jars 선택후 ojdbc11.jar 파일이 존재하는 경로 추가  
			 *  
			 * 
			 */
			
			// 2) 등록된 드라이버 확인. 
			// DriverManager.drivers()
			DriverManager.drivers().forEach(System.out::println);
			//		String[] driverNames = {"oracle.jdbc.driver.OracleDriver", "mysql.jdbc.Driver"};
			//		for(String driver : driverNames) {
			//			Class.forName(driver);
			//		}
			
			// new OracleDriver();
			// new Driver();
	
			
			try {
				// 3) DBMS 연결 -> Connection 객체 생성
				// DriverManager.getConnection("jdbcurl주소", "계정", "비밀번호");
				Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "C##JDBC", "JDBC");
				// url -> jdbc(프로토콜):oracle:thin(서브프로토콜):@localhost:1521:xe(서브네임 : 연결하고자하는 데이터베이스의 연결정보)
				System.out.println(conn);

				// 4) Statement 객체 생성
				Statement stmt = conn.createStatement();

				// 5) DB에 SQL문을 전달하면서 실행
				boolean result = stmt.execute("SELECT 'HELLO JDBC' AS TEST FROM DUAL");

				// 6) 결과값 받기 (selectd인 경우 getResultSet(); || DML인 경우 getUpdateCount())
				if (result) {
					// select일때
					ResultSet rset = stmt.getResultSet();
					// update일때
					int updatedCount = stmt.getUpdateCount();

					if (rset.next()) { // 조회결과가 있는지 확인
						System.out.println(rset.getString("TEST")); // 컬럼값의 데이터를 반환, 대소문자 가리지 않음
						System.out.println(rset.getString(1)); // 컬럼값의 데이터를 반환, 숫자기술가능
						System.out.println(updatedCount); // 수정된것 없으면 -1 반환
					}
				}

				// 5+6) excute + getResultSet() -> executeQuery(sql); SQL이 SELECT문인 경우 
				// 		execute(sql) + getUpdateCount() -> executeUpdate(sql); SQL이 DML인 경우
				ResultSet rset = stmt.executeQuery("SELECT 'HELLO JDBC' AS STR FROM DUAL");
				if (rset.next()) {
					System.out.println(rset.getString("STR"));
				}
				
				// 7) 자원반납 close() -- 데이터베이스와 연결되어있은 방식은 TCP 프로토콜을 쓰는데 자원을 많이 사용한다.
				// 						따라서 연결자원을 받아주도록 해야한다. 
				// 생성된 자원의 역순으로 닫아줘야한다 -- 순서에 맞게 기술해야한다.
				rset.close();
				stmt.close();
				conn.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
