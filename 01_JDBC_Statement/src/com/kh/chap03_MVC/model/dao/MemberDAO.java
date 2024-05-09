package com.kh.chap03_MVC.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.kh.common.template.JDBCTemplate;
import com.kh.model.vo.Member;

/*
 * DAO(Data Access Object)? 
 *   - Service에 의해 호출되며, 맡은 기능을 수행하기 위해 db에 직접 접근하여 sql문을 호출한 후 
 *     처리 결과값을 반환시켜주는 객체
 *   - 
 * 
 */
public class MemberDAO {

	/**
	 * 사용자가 view에서 입력한 값을 가지고 db에 INSERT문을 실행하는 메서드
	 * 
	 * @param conn
	 * @param m
	 * @return 처리된 행의 갯수
	 */
	public int insertMember(Connection conn, Member m) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO MEMBER VALUES("
				+ "SEQ_UNO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE, DEFAULT)";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, m.getUserId());
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getEmail());
			pstmt.setString(5, m.getBirthday());
			pstmt.setString(6, String.valueOf(m.getGender())); // 문자열로 만들어준다.
			pstmt.setString(7, m.getPhone());
			pstmt.setString(8, m.getAddress());

			result = pstmt.executeUpdate(); // 처리결과값을 담아준다. (처리된 행이 없으면 0, 있다면 1 이상)

		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
		
	}

}
