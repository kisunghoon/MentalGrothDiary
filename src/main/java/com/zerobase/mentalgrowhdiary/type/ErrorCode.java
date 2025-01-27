package com.zerobase.mentalgrowhdiary.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXIST("이지 존재하는 회원입니다."),
    USER_NOT_FOUND_ID("존재하지 않는 ID입니다."),
    EMAIL_ALERADY_EXIST("중복된 이메일 계정입니다."),
    COUNSELOR_NOT_FOUND("상담사를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다."),
    FORBIDDEN("접근 권한이 없습니다."),
    INTERNAL_SERVER_ERROR("내부 서버 오류가 발생하였습니다.");

    private final String desc;
}
