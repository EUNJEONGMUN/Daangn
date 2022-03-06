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
    EMPTY_KEYWORD(false, 2018, "키워드를 입력해주세요."),

    // [POST] /towns
    POST_TOWNS_EMPTY_CONTENT(false, 2100, "내용을 입력해주세요."),

    // [POST] /around
    POST_AROUND_EMPTY_TITLE(false, 2200, "제목을 입력해주세요."),
    POST_AROUND_EMPTY_CONTENT(false, 2201, "내용을 입력해주세요."),
    POST_AROUND_CHAT_EMPTY_CONTENT(false, 2202, "내용을 입력해주세요."),
    POST_FAIL_AROUND_CHAT_SELF(false, 2203, "자신에게 채팅을 보낼 수 없습니다."),

    // PATCH
    PATCH_ATTENTION_EMPTY_STATUS(false, 2204, "상태를 입력해주세요."),

    // [POST] /products
    POST_PRODUCTS_EMPTY_CONTENT(false, 2300, "내용을 입력해주세요."),
    POST_PRODUCTS_EMPTY_TITLE(false, 2301, "제목을 입력해주세요."),
    POST_ATTENTION_EMPTY_STATUS(false, 2302, "상태를 입력해주세요."),
    POST_DEAL_EMPTY_STATUS(false, 2303, "상태를 입력해주세요."),



    // [POST] /deals
    POST_DEAL_DUPLICATE(false, 2400, "이미 존재하는 거래입니다."),
    POST_DEAL_EMPTY(false, 2401,"존재하지 않는 거래입니다."),

    // [post] /stores
    POST_STORE_EMPTY_STORENAME(false, 2500, "가게 이름을 입력해주세요"),
    POST_STORE_NUMBER_EXCEEDED(false, 2501, "비즈 프로필은 3개까지 생성 가능합니다."),
    POST_NEWS_EMPTY_STOREID(false, 2502, "가게 아이디를 입력해주세요."),
    POST_NEWS_EMPTY_TITLE(false, 2503, "가게 소식 제목을 입력해주세요."),
    POST_NEWS_EMPTY_CONTENT(false, 2504, "가게 소식 내용을 입력해주세요."),

    // [patch] /stores
    PATCH_STORE_EMPTY_USER(false, 2510, "비즈프로필이 존재하지 않습니다."),
    PATCH_NEWS_NOT_CORRECT_USER(false, 2511, "자신에 가게에 글을 쓸 수 있습니다."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),
    DUPLICATED_KEYWORDS(false, 3015, "중복된 키워드가 존재합니다."),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    // [POST] /towns
    CREATE_FAIL_TOWN_POST_LIKED(false, 4100, "동네 생활 글 좋아요 생성에 실패하였습니다."),
    CREATE_FAIL_TOWN_POST_COM_LIKED(false, 4101, "동네 생활 댓글 좋아요 생성에 실패하였습니다."),

    // [PATCH] /towns
    MODIFY_FAIL_TOWN_POST(false, 4110, "동네 생활 글 수정에 실패 했습니다."),
    DELETE_FAIL_TOWN_POST(false, 4111, "동네 생활 글 삭제에 실패했습니다."),
    MODIFY_FAIL_TOWN_COM(false, 4112, "동네 생활 댓글 수정에 실패했습니다."),
    DELETE_FAIL_TOWN_COM(false, 4113, "동네 생활 댓글 삭제에 실패했습니다."),
    MODIFY_FAIL_TOWN_POST_LIKED(false, 4114, "동네 생활 글 좋아요 변경에 실패하였습니다."),
    MODIFY_FAIL_TOWN_POST_COM_LIKED(false, 4115, "동네 생활 댓글 좋아요 변경에 실패하였습니다."),

    // [POST] /aourd
    POST_FAIL_AROUND_CHATLIST(false, 4200, "내 근처 글 채팅 방 생성 실패"),
    POST_FAIL_AROUND_CHAT(false, 4201, "내 근처 글 채팅 전송 실패"),

    // [GET] /around
    FIND_FAIL_AROUND_POST_USER(false, 4210, "내 근처 글을 작성한 사용자가 없습니다"),


    // [PUT] /products
    MODIFY_FAIL_PRODUCT_ATTENTION(false, 4300, "중고거래 관심 등록 실패"),
    MODIFY_FAIL_PRODUCT_POST(false, 4301, "중고 거래 글 수정 실패"),

    // [DELETE] /products
    DELETE_FAIL_PRODUCT_POST(false, 4302, "중고거래 글 삭제를 실패했습니다."),

    // [GET] /deals
    FIND_FAIL_DEAL_USER(false, 4400, "중고 거래 기록이 없습니다."),

    // [PATCH]
    DELETE_FAIL_DEAL(false, 4410, "중고 거래 기록 삭제 실패"),
    //[POST]
    CREATE_FAIL_DEAL(false, 4411, "중고 거래 생성 실패"),

    // [PUT] / users
    CREATE_FAIL_KEYWORDS(false, 4500, "키워드 설정에 실패했습니다."),

    // [PATCH] /users
    PATCH_FAIL_MYINFO(false, 4510, "사용자 정보를 바꾸는 데 실패했습니다."),

    //[DELETE] /users
    DELETE_FAIL_KEYWORDS(false, 4520, "키워드 삭제에 실패했습니다."),

    // [patch] /stores
    FAIL_TO_STORE_MODIFY(false, 4530, "가게 정보를 수정하는 데 실패했습니다."),
    FAIL_TO_STORE_DELETE(false, 4531, "가게 정보를 삭제하는 데 실패했습니다."),
    FAIL_TO_NEWS_MODIFY(false, 4532, "가게 소식을 수정하는 데 실패했습니다.");

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
