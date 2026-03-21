package chdaeseung.accountbook.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인 해주세요."),
    TRANSACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "거래내역이 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리가 없습니다."),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "사용중인 아이디입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "사용중인 이메일입니다."),
    CATEGORY_IN_USE(HttpStatus.CONFLICT, "사용중인 카테고리는 삭제할 수 없습니다."),
    DUPLICATE_CATEGORY(HttpStatus.CONFLICT, "이미 등록된 카테코리입니다."),
    INCORRECT_ACCOUNT(HttpStatus.CONFLICT, "아이디 또는 비밀번호가 일치하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
