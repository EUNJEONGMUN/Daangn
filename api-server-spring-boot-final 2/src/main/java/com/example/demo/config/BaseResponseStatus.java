package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),

    // towns
    TOWNS_CATEGORY_ERROR(false, 210, "올바른 카테고리 범위를 벗어났습니다.-동네생활"),
    
    // [POST] /towns
    POST_TOWNS_EMPTY_CATEGORY(false, 215, "카테고리를 선택해주세요.-동네생활"),
    POST_TOWNS_EMPTY_CONTENT(false, 216, "내용을 입력해주세요."),

    // [POST] /around
    POST_AROUND_CHAT_EMPTY_CONTENT(false, 220, "내용을 입력해주세요."),
    POST_FAIL_AROUND_CHAT_SELF(false, 221, "자신에게 채팅을 보낼 수 없습니다."),

    // products
    PRODUCTS_CATEGORY_ERROR(false, 230, "올바른 카테고리 범위를 벗어났습니다.-중고거래"),

    // [POST] /products
    POST_PRODUCTS_EMPTY_CATEGORY(false, 235, "카테고리를 선택해주세요.-중고거래"),
    POST_PRODUCTS_EMPTY_CONTENT(false, 236, "내용을 입력해주세요."),
    POST_PRODUCTS_EMPTY_TITLE(false, 236, "제목을 입력해주세요."),

    // [POST] /deals
    POST_DEALS_EMPTY_USERID(false, 240, "유저 아이디를 입력해주세요."),



    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    // [PUT] /towns
    MODIFY_FAIL_TOWNPOSTLIKED(false, 410, "동네 생활 글 좋아요 실패"),
    MODIFY_FAIL_TOWNCOMLIKED(false, 411, "동네 생활 댓글 좋아요 실패"),

    // [POST] /aourd
    POST_FAIL_AROUND_CHATLIST(false, 420, "내 근처 글 채팅 방 생성 실패"),
    POST_FAIL_AROUND_CHAT(false, 421, "내 근처 글 채팅 전송 실패"),

    // [GET] /around
    FIND_FAIL_AROUND_POST_USER(false, 430, "내 근처 글을 작성한 사용자가 없습니다"),


    // [PUT] /products
    MODIFY_FAIL_PRODUCT_ATTENTION(false, 440, "중고거래 관심 등록 실패");
    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
