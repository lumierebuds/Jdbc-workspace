package com.kh.chap03_MVC.model.dao;

// 내부에 존재하는 메서드들을 static하게 접근하려한다. 
import static com.kh.common.template.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
			close(pstmt);
		}

		return result;
		
	}

	public List<Member> selectAll(Connection conn) {


		List<Member> list = new ArrayList<Member>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "SELECT * FROM MEMBER WHERE STATUS = 'Y'";
		Member m = null;
		
		
		try {
			pstmt = conn.prepareStatement(sql);

			rset = pstmt.executeQuery();
			
			while(rset.next()) {

				// 현재 rset의 커서가 가리키는 지점에서 데이터 추출
				m = new Member();
				m.setUserNo(rset.getInt("USER_NO"));
				m.setUserId(rset.getString("USER_ID"));
				m.setUserPwd(rset.getString("USER_PWD"));
				m.setUserName(rset.getString("USER_NAME"));
				m.setGender(rset.getString("GENDER").charAt(0));
				m.setEmail(rset.getString("EMAIL"));
				m.setBirthday(rset.getString("BIRTHDAY"));
				m.setPhone(rset.getString("PHONE"));
				m.setAddress(rset.getString("ADDRESS"));
				m.setEnrollDate(rset.getDate("ENROLL_DATE"));
				m.setModifyDate(rset.getDate("MODIFY_DATE"));
				m.setStatus(rset.getString("STATUS").charAt(0));

				list.add(m);
			}
			

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, pstmt, rset);
			// 통합 닫기를 할때, connection은 null로 보내 닫으면,
			// connection에 대해 닫기가 수행되지 않음

			// close(rset);
			// close(pstmt);

		}
		return list;
	}

	public Member selectByUserId(Connection conn, String userId) {

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Member m = null;
		String sql = "SELECT * FROM MEMBER WHERE USER_ID = ? AND STATUS = 'Y'";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);

			rset = pstmt.executeQuery();

			if (rset.next()) {
				m = new Member();
				m.setUserNo(rset.getInt("USER_NO"));
				m.setUserId(rset.getString("USER_ID"));
				m.setUserPwd(rset.getString("USER_PWD"));
				m.setUserName(rset.getString("USER_NAME"));
				m.setGender(rset.getString("GENDER").charAt(0));
				m.setEmail(rset.getString("EMAIL"));
				m.setBirthday(rset.getString("BIRTHDAY"));
				m.setPhone(rset.getString("PHONE"));
				m.setAddress(rset.getString("ADDRESS"));
				m.setEnrollDate(rset.getDate("ENROLL_DATE"));
				m.setModifyDate(rset.getDate("MODIFY_DATE"));
				m.setStatus(rset.getString("STATUS").charAt(0));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, pstmt, rset);
		}

		return m;

	}

	public List<Member> selectByUserName(Connection conn, String keyword) {


		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Member m = null;
		List<Member> list = new ArrayList<Member>();
		String sql = "SELECT * FROM MEMBER WHERE USER_NAME LIKE ? AND STATUS = 'Y'";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + keyword + "%"); // ?에 들어갈 값의 형태를 만들어줬다.

			rset = pstmt.executeQuery();

			while (rset.next()) {
				m = new Member();
				m.setUserNo(rset.getInt("USER_NO"));
				m.setUserId(rset.getString("USER_ID"));
				m.setUserPwd(rset.getString("USER_PWD"));
				m.setUserName(rset.getString("USER_NAME"));
				m.setGender(rset.getString("GENDER").charAt(0));
				m.setEmail(rset.getString("EMAIL"));
				m.setBirthday(rset.getString("BIRTHDAY"));
				m.setPhone(rset.getString("PHONE"));
				m.setAddress(rset.getString("ADDRESS"));
				m.setEnrollDate(rset.getDate("ENROLL_DATE"));
				m.setModifyDate(rset.getDate("MODIFY_DATE"));
				m.setStatus(rset.getString("STATUS").charAt(0));

				list.add(m);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(null, pstmt, rset);
		}

		return list;
	}

	public int selectUser(Connection conn, String userId, String userPwd) {

		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		String sql = "SELECT COUNT(*) FROM MEMBER WHERE USER_ID = ? AND USER_PWD = ? AND STATUS = 'Y'"; // 회원 아이디, 비밀번호가
																										// 같고 존재하면
																										// count해서 확인

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, userPwd);

			rset = pstmt.executeQuery();

			if (rset.next()) {
				result = rset.getInt(1); // 회원이 존재한다면 1, 없으면 0이 나올것이다.
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(null, pstmt, rset);
		}


		return result;
	}

	public int updateMember(Connection conn, Member m) {
		
		int result = 0; 
		PreparedStatement pstmt = null;
		String sql = "UPDATE MEMBER SET \r\n"
				+ "    EMAIL = ?, \r\n"
				+ "    PHONE = ?, \r\n"
				+ "    ADDRESS = ?, \r\n"
				+ "    MODIFY_DATE = SYSDATE\r\n"
				+ "	   WHERE USER_ID = ? "; // MODIFY_DATE : 수정한 날짜(이것도 따로 테이블로 관리가 가능하지만 지금은 여기서만 해논다)
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, m.getEmail());
			pstmt.setString(2, m.getPhone());
			pstmt.setString(3, m.getAddress());
			pstmt.setString(4, m.getUserId());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(pstmt); // ResultSet 자원은 없기 때문에 PreparedStatement만 반환
		}
		
		return result;
		
	}

	public int deleteMember(Connection conn, Member m) {
		
		int result = 0; 
		PreparedStatement pstmt = null;
		String sql = "UPDATE MEMBER SET\r\n"
				+ "    STATUS = 'N',\r\n"
				+ "    MODIFY_DATE = SYSDATE,\r\n" // 수정될때도 정보가 변경된거기 때문에 MODIFY_DATE 컬럼도 SYSDATE로 변경
				+ "    WHERE (USER_ID = ? AND USER_PWD = ?) \r\n"
				+ "    AND STATUS = 'Y';";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, m.getUserId());
			pstmt.setString(2, m.getUserPwd());

			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}

}
