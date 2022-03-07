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
    FAIL_MESSAGE_AUTH(false, 2011, "휴대폰 인증에 실패하였습니다."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    EMPTY_KEYWORD(false, 2018, "키워드를 입력해주세요."),
    POST_USERS_EMPTY_PHONENUMBER(false, 2019, "휴대폰 번호를 입력해주세요."),
    POST_USERS_INVALID_PHONEMUNBER(false, 2020, "휴대폰 번호 형식을 확인해주세요."),
    POST_USERS_NOT_EXISTS_PHONENUMBER(false, 2022, "회원가입을 먼저 진행해주세요."),

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
    DUPLICATED_EMAIL(false, 3010, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3011,"없는 아이디거나 비밀번호가 틀렸습니다."),
    DUPLICATED_KEYWORDS(false, 3012, "중복된 키워드가 존재합니다."),
    POST_USERS_EXISTS_PHONE_NUMBER(false, 3013, "이미 가입한 적이 있거나 탈퇴한 번호입니다."),
    POST_USERS_SECESSION(false, 3014, "탈퇴한 계정입니다."),
    /**
     * 4000
     */


    /**
     * 5000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 5000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 5001, "서버와의 연결에 실패하였습니다."),

    //users
    MODIFY_FAIL_USERNAME(false,5100,"유저네임 수정 실패"),
    PASSWORD_ENCRYPTION_ERROR(false, 5101, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 5102, "비밀번호 복호화에 실패하였습니다."),
    PHONENUMBER_DECRYPTION_ERROR(false, 5103, "전화번호 복호화에 실패했습니다."),
    PATCH_FAIL_MYINFO(false, 5104, "사용자 정보를 바꾸는 데 실패했습니다."),
    CREATE_FAIL_KEYWORDS(false, 5105, "키워드 설정에 실패했습니다."),
    DELETE_FAIL_KEYWORDS(false, 5106, "키워드 삭제에 실패했습니다."),


    ///towns
    CREATE_FAIL_TOWN_POST_LIKED(false, 5200, "동네 생활 글 좋아요 생성에 실패하였습니다."),
    CREATE_FAIL_TOWN_POST_COM_LIKED(false, 5201, "동네 생활 댓글 좋아요 생성에 실패하였습니다."),
    POST_FAIL_SIGNUP(false, 5202, "회원가입에 실패하였습니다."),
    MODIFY_FAIL_TOWN_POST(false, 5204, "동네 생활 글 수정에 실패 했습니다."),
    DELETE_FAIL_TOWN_POST(false, 5205, "동네 생활 글 삭제에 실패했습니다."),
    MODIFY_FAIL_TOWN_COM(false, 5206, "동네 생활 댓글 수정에 실패했습니다."),
    DELETE_FAIL_TOWN_COM(false, 5207, "동네 생활 댓글 삭제에 실패했습니다."),
    MODIFY_FAIL_TOWN_POST_LIKED(false, 5208, "동네 생활 글 좋아요 변경에 실패하였습니다."),
    MODIFY_FAIL_TOWN_POST_COM_LIKED(false, 5209, "동네 생활 댓글 좋아요 변경에 실패하였습니다."),


    // [PUT] /products
    MODIFY_FAIL_PRODUCT_ATTENTION(false, 5300, "중고거래 관심 등록을 실패했습니다."),
    MODIFY_FAIL_PRODUCT_POST(false, 5301, "중고 거래 글 수정에 실패했습니다."),
    DELETE_FAIL_PRODUCT_POST(false, 5302, "중고거래 글 삭제를 실패했습니다."),

    // [GET] /deals
    FIND_FAIL_DEAL_USER(false, 5400, "중고 거래 기록이 없습니다."),
    DELETE_FAIL_DEAL(false, 5401, "중고 거래 기록 삭제를 실패했습니다."),
    CREATE_FAIL_DEAL(false, 5402, "중고 거래 생성을 실패했습니다."),


    // [patch] /stores
    FAIL_TO_STORE_MODIFY(false, 5500, "가게 정보를 수정하는 데 실패했습니다."),
    FAIL_TO_STORE_DELETE(false, 5501, "가게 정보를 삭제하는 데 실패했습니다."),
    FAIL_TO_NEWS_MODIFY(false, 5502, "가게 소식을 수정하는 데 실패했습니다."),

    // [POST] /aourd
    POST_FAIL_AROUND_CHATLIST(false, 5601, "내 근처 글 채팅 방 생성 실패"),
    POST_FAIL_AROUND_CHAT(false, 5602, "내 근처 글 채팅 전송 실패"),
    FIND_FAIL_AROUND_POST_USER(false, 5603, "내 근처 글을 작성한 사용자가 없습니다");









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
