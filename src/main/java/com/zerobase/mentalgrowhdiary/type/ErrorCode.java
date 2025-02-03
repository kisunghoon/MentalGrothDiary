package com.zerobase.mentalgrowhdiary.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXIST("이미 존재하는 회원입니다."),
    USER_NOT_FOUND_ID("존재하지 않는 ID입니다."),
    EMAIL_ALERADY_EXIST("중복된 이메일 계정입니다."),
    COUNSELOR_NOT_FOUND("상담사를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다."),
    ALERADY_SELECTED_COUNSELOR("이미 선택된 상담자 입니다."),
    MAX_SELECTED_COUNSELOR("상담사는 최대 한명만 선택 가능합니다."),
    NO_SELECTED_COUNSELOR("상담사 변경은 상담사 선정 후 가능합니다."),
    NO_SELECTED_USER_COUNSELOR("사용자나 상담사를 찾을 수 없습니다."),
    NO_ACTIVE_COUNSELOR("활성화된 상담자가 없습니다."),
    DIARY_NOT_FOUND("일기를 찾을 수 없습니다."),
    DIARY_ALERADY_REGISTER("이미 해당 날짜에 작성된 일기가 있습니다."),
    DIARY_HAS_FEEDBACK("해당 일기는 피드백이 존재하여 삭제할 수 없습니다."),
    NOT_YET_SELECTED_COUNSELOR("상담사가 아직 선정되지 않았습니다, 일기를 작성하기 전에 상담사를 선정해 주세요."),
    FEEDBACK_ALREADY_REQUEST("피드백이 이미 존재합니다."),
    FEEDBACK_PROGESS("피드백이 진행중인 상태일때는 상담사를 변경할 수  없습니다."),
    NO_AVAILABLE_SLOTS("상담시간이 아닙니다, 다른 시간에 신청해주세요"),
    FORBIDDEN("접근 권한이 없습니다."),
    INTERNAL_SERVER_ERROR("내부 서버 오류가 발생하였습니다.");

    private final String desc;
}
