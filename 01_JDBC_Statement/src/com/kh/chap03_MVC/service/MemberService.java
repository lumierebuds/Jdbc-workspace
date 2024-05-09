package com.kh.chap03_MVC.service;

import java.sql.Connection;

import com.kh.chap03_MVC.model.dao.MemberDAO;
import com.kh.common.template.JDBCTemplate;
import com.kh.model.vo.Member;
/*
 * Service : 컨트롤러에 의해 호출되는 최초의 메서드. 
 * 			 여러 dao에 존재하는 메서드들을 호출하여 논리적으로 연관이 있는 비즈니스 로직을 만든다. 
 * 			 처리 결과값을 컨트롤러에게 반환해주는 역할을 한다.  
 */

public class MemberService {

	private MemberDAO mDao = new MemberDAO();

	public int insertMember(Member m) {

		// Connection 객체 생성
		Connection conn = JDBCTemplate.getConnection();

		// DAO 호출시 Connection 객체와 기존에 넘기고자 했던 매개변수(m)을 같이 넘겨준다.
		int result = mDao.insertMember(conn, m);

		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		JDBCTemplate.close(conn);

		return result;
	}

}
