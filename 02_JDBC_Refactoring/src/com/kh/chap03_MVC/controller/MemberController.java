package com.kh.chap03_MVC.controller;

import java.util.List;

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

	/**
	 * 사용자의 회원 전체 요청기능을 처리하는 메서드
	 * 
	 * @return
	 */
	public void selectAll() {

		// 결과값을 담을 변수
		List<Member> list = null;
		list = mService.selectAll();

		if (list.isEmpty()) {
			new MemberView().displayNoData("조회결과가 없습니다.");
		} else {
			new MemberView().displayList(list);
		}

	}

	/**
	 * 사용자의 아이디로 검색요청을 해주는 메소드
	 * 
	 * @param userId : 사용자가 입력했던 검색하고자 하는 아이디
	 */
	public void selectByUserId(String userId) {
		// 결과값을 담을 변수
		// Member m = null;
		Member m = mService.selectByUserId(userId);

		if (m == null) {
			new MemberView().displayNoData("조회결과 일치하는 Member가 없습니다.");
		} else {
			new MemberView().displayOne(m);
		}
	}

	/**
	 * 사용자의 이름 키워드로 검색요청해주는 메소드
	 * 
	 * @param keyword
	 */
	public void selectByUserName(String keyword) {
		// 결과값을 담을 변수
		List<Member> list = null;
		list = mService.selectByUserName(keyword);

		if (list.isEmpty()) {
			new MemberView().displayNoData("조회결과 일치하는 Member가 없습니다.");
		} else {
			new MemberView().displayList(list);
		}

	}

	public void updateMember(String userId, String userPwd, String email, String phone, String address) {
		// 가공처리
		Member m = new Member();
		m.setUserId(userId);
		m.setUserPwd(userPwd);
		m.setEmail(email);
		m.setPhone(phone);
		m.setAddress(address);

		// 회원의 아이디와 비밀번호를 가지고 인증된 사용자인지 검사.
		// SELECT * FROM MEMBER WHERE USER_ID=? AND USER_PWD=? AND STATUS = 'Y';
		
		int result = mService.selectUser(userId, userPwd);
		
		// 존재하는 회원
		if (result > 0) {
			// 회원정보 수정작업.
			// UPDATE MEMBER SET 칼럼명 = ?, ... WHERE USER_ID = ?
			result = mService.updateMember(m);

			if (result > 0) { // 수정에 성공했으면
				new MemberView().displaySuccess("회원정보 변경 성공");
			} else {
				new MemberView().displayFail("회원정보 변경 실패");
			}

		} else {
			new MemberView().displayFail("존재하지 않는 회원입니다.");
		}
	}

	public void deleteMember(String userId, String userPwd) {
		// 가공처리
		Member m = new Member();
		m.setUserId(userId);
		m.setUserPwd(userPwd);

		int result = mService.selectUser(userId, userPwd);

		// 존재하는 회원

		if (result > 0) {

			result = mService.deleteMember(m);
			if (result > 0) {
				new MemberView().displaySuccess("회원탈퇴 성공");
			} else {
				new MemberView().displayFail("회원 탈퇴 실패");
			}

		} else {

			new MemberView().displayFail("존재하지 않는 회원입니다.");

		}

	}

}
