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
    CATEGORY_RANGE_ERROR(false, 2004, "올바른 카테고리 범위를 벗어났습니다."),
    EMPTY_CATEGORY(false, 2005, "카테고리를 선택해주세요."),
    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),

    // [POST] /towns

    POST_TOWNS_EMPTY_CONTENT(false, 216, "내용을 입력해주세요."),

    // [POST] /around
    POST_AROUND_EMPTY_TITLE(false, 220, "제목을 입력해주세요."),
    POST_AROUND_EMPTY_CONTENT(false, 221, "내용을 입력해주세요."),
    POST_AROUND_CHAT_EMPTY_CONTENT(false, 222, "내용을 입력해주세요."),
    POST_FAIL_AROUND_CHAT_SELF(false, 223, "자신에게 채팅을 보낼 수 없습니다."),


    // [POST] /products
    POST_PRODUCTS_EMPTY_CONTENT(false, 236, "내용을 입력해주세요."),
    POST_PRODUCTS_EMPTY_TITLE(false, 236, "제목을 입력해주세요."),
    POST_ATTENTION_EMPTY_STATUS(false, 237, "상태를 입력해주세요."),
    POST_DEAL_EMPTY_STATUS(false, 238, "상태를 입력해주세요."),
    // PATCH
    PATCH_ATTENTION_EMPTY_STATUS(false, 239, "상태를 입력해주세요."),

    // [POST] /deals
    POST_DEAL_DUPLICATE(false, 2400, "이미 존재하는 거래입니다."),
    POST_DEAL_EMPTY(false, 2401,"존재하지 않는 거래입니다."),
    // [post] /stores
    POST_STORE_EMPTY_STORENAME(false, 2500, "가게 이름을 입력해주세요"),
    POST_STORE_NUMBER_EXCEEDED(false, 2501, "비즈 프로필은 3개까지 생성 가능합니다."),
    POST_NEWS_EMPTY_STOREID(false, 2510, "가게 아이디를 입력해주세요."),
    POST_NEWS_EMPTY_TITLE(false, 2511, "가게 소식 제목을 입력해주세요."),
    POST_NEWS_EMPTY_CONTENT(false, 2512, "가게 소식 내용을 입력해주세요."),

    // [patch] /stores
    PATCH_STORE_EMPTY_USER(false, 2510, "비즈프로필이 존재하지 않습니다."),
    PATCH_NEWS_NOT_CORRECT_USER(false, 2320, "자신에 가게에 글을 쓸 수 있습니다."),

    // [PUT] /users
    EMPTY_KEYWORD(false, 270, "키워드를 입력해주세요."),
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

    // [PATCH] /towns
    MODIFY_FAIL_TOWN_POST(false, 415, "동네 생활 글 수정에 실패 했습니다."),
    DELETE_FAIL_TOWN_POST(false, 416, "동네 생활 글 삭제에 실패했습니다."),
    // [POST] /aourd
    POST_FAIL_AROUND_CHATLIST(false, 420, "내 근처 글 채팅 방 생성 실패"),
    POST_FAIL_AROUND_CHAT(false, 421, "내 근처 글 채팅 전송 실패"),

    // [GET] /around
    FIND_FAIL_AROUND_POST_USER(false, 430, "내 근처 글을 작성한 사용자가 없습니다"),


    // [PUT] /products
    MODIFY_FAIL_PRODUCT_ATTENTION(false, 440, "중고거래 관심 등록 실패"),
    MODIFY_FAIL_PRODUCT_POST(false, 441, "중고 거래 글 수정 실패"),

    // [DELETE] /products
    DELETE_FAIL_PRODUCT_POST(false, 442, "중고거래 글 삭제를 실패했습니다."),

    // [GET] /deals
    FIND_FAIL_DEAL_USER(false, 450, "중고 거래 기록이 없습니다."),
    DELETE_FAIL_DEAL(false, 451, "중고 거래 기록 삭제 실패"),

    // [PUT] / users
    SET_FAIL_KEYWORDS(false, 460, "키워드 설정에 실패했습니다."),

    // [PATCH] /users
    PATCH_FAIL_MYINFO(false, 465, "사용자 정보를 바꾸는 데 실패했습니다."),
    // [patch] /stores
    FAIL_TO_STORE_MODIFY(false, 4700, "가게 정보를 수정하는 데 실패했습니다."),
    FAIL_TO_STORE_DELETE(false, 4701, "가게 정보를 삭제하는 데 실패했습니다."),
    FAIL_TO_NEWS_MODIFY(false, 4710, "가게 소식을 수정하는 데 실패했습니다.");

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
