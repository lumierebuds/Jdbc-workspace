package com.kh.chap03_MVC.controller;

import com.kh.chap03_MVC.service.MemberService;
import com.kh.chap03_MVC.view.MemberView;
import com.kh.model.vo.Member;

/*
 * Controller : View를 통해서 요청한 기능처리를  담당
 * 				해당 메소드로 전달된 데이터들을 가공처리 한 후, Service메소드 호출시 전달한다. 
 * 				Service로부터 반환받은 결과에 따라 사용자가 보게될 응답화면을 지정
 */
public class MemberController {

	private MemberService mService = new MemberService();

	/**
	 * 사용자의 회원추가 요청을 처리해주는 메서드.
	 * 
	 * @param userId   : 추가할 아이디
	 * @param userPwd  : 추가할 비밀번호
	 * @param userName : ...
	 * @param gender
	 * @param email
	 * @param birthday
	 * @param phone
	 * @param address
	 */
	public void insertMember(String userId, String userPwd, String userName, char gender, String email, String birthday,
			String phone, String address) {

		// Controller 순서
		// 1. 전달받은 데이터 가공처리

		Member m = new Member();
		m.setUserId(userId);
		m.setUserPwd(userPwd);
		m.setUserName(userName);
		m.setGender(gender);
		m.setAddress(address);
		m.setBirthday(birthday);
		m.setEmail(email);
		m.setPhone(phone);

		// 2. Service의 insertMember 메서드 호출하기
		int result = mService.insertMember(m);

		// 3. 결과값에 따라서 사용자가 보게될 화면을 지정
		if (result > 0) { // 성공
			// 성공 메시지를 띄워주는 화면을 호출
			new MemberView().displaySuccess("회원 추가 성공");
		} else { // 실패
			// 실패 메시지를 띄워즈는 화면을 호출
			new MemberView().displayFail("회원 추가 실패");
		}
	}

}
