package com.example.demo.src.user;

import com.example.demo.src.product.model.Res.GetProductListRes;
import com.example.demo.src.user.model.*;
import com.example.demo.src.user.model.Req.*;
import com.example.demo.src.user.model.Res.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {
    private JdbcTemplate jdbcTemplate;
    private GetMyInfo getMyInfo;
    private GetMyCount getMyCount;
    private List<GetMyManner> getMyManners;
    private List<GetMyComment> getMyComments;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 회원가입 API
     * [POST] /users/sign-up
     *
     * @return BaseResponse<String>
     */
    public int createUser(PostUserReq postUserReq) {
        String Query = "insert into User (userPhoneNumber, userName, userProfileImageUrl) values (?,?,?);";
        Object[] Params = new Object[]{postUserReq.getPhoneNumber(), postUserReq.getUserName(), postUserReq.getProfileImg()};

        this.jdbcTemplate.update(Query,Params);
        String lastInsertIdQuery = "select last_insert_id()";

        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

    }

    // 사용자 동네 입력
    public int createUserArea(int userId, int jusoCodeId) {
        String Query = "insert into UserArea (userId, jusoCodeId) values (?,?);";
        Object[] Params = new Object[]{userId, jusoCodeId};
        return this.jdbcTemplate.update(Query, Params);
    }

    // 휴대폰 번호 중복체크
    public int checkPhoneNumber(String phoneNumber) {
        String Query = "select exists(select * from User where UserPhoneNumber = ?);";
        String Params = phoneNumber;
        return this.jdbcTemplate.queryForObject(Query,
                int.class,
                Params);
    }


    // 사용자 정보 가져오기
    public User getPhoneNumber(PostSignInReq postSignInReq) {
        // User에 담길 정보들
        String Query = "select User.userId, User.userName, User.userProfileImageUrl as profileImg from User where userPhoneNumber = ?;";
        String Params = postSignInReq.getPhoneNumber();
        return this.jdbcTemplate.queryForObject(Query,
                (rs, rowNum) -> new User(
                        rs.getInt("userId"),
                        rs.getString("userName"),
                        rs.getString("profileImg")
                ),
                Params
        );
    }

    /**
     * 프로필 조회 API
     * [GET] /users/:userId
     *
     * @return BaseResponse<GetUserInfoRes>
     */
    public GetUserInfoRes getUserInfo(int userId) {
        // 사용자 기본 정보
        String Query = "select User.userId, JusoCode.jusoName, User.userProfileImageUrl as userImg, User.userName as name, User.userMannerScore as score, User.userCertificationCount as certificationCount\n" +
                "from UserArea\n" +
                "    inner join JusoCode on UserArea.jusoCodeId = JusoCode.jusoCodeId\n" +
                "    inner join User on UserArea.userId = User.userId\n" +
                "where UserArea.userId = ? AND UserArea.status = 'Y';";

        // 판매 상품 개수 + 배지개수 + 후기 개수
        String Query1 = "select count(productPostId) as postCount, BC.badgeCount, CC.comCount\n" +
                "from ProductPost PP\n" +
                "left join (\n" +
                "    select UB.userId, count(badgeId) as badgeCount\n" +
                "    from UserBadge UB\n" +
                "    group by UB.userId\n" +
                "    ) BC on BC.userId = PP.userId\n" +
                "left join (\n" +
                "    select DC.receiveUserId, count(*) as comCount\n" +
                "    from DealCom DC\n" +
                "    group by DC.receiveUserId\n" +
                "    ) CC on CC.receiveUserId = PP.userId\n" +
                "where PP.userId = ?\n" +
                "group by PP.userId;";


        // 받은 매너 평가 + 개수
        String Query2 = "select ML.mannerContent as manner, count(UM.mannerListId) as mannerCount\n" +
                "from UserManner UM join MannerList ML on UM.mannerListId = ML.mannerListId\n" +
                "where UM.userId = ?\n" +
                "group by UM.mannerListId;";

        // 받은 거래 후기 내용, 보낸이 정보
        String Query3 = "select User.userId as traderId, User.userName as traderName, User.userProfileImageUrl as traderImg,\n" +
                "     case\n" +
                "           when TIMESTAMPDIFF(SECOND, DC.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(SECOND, DC.createdAt, current_timestamp()),'초 전')\n" +
                "           when TIMESTAMPDIFF(MINUTE, DC.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(MINUTE, DC.createdAt, current_timestamp()),'분 전')\n" +
                "            when TIMESTAMPDIFF(HOUR, DC.createdAt, current_timestamp())<24\n" +
                "            then concat(TIMESTAMPDIFF(HOUR, DC.createdAt, current_timestamp()), '시간 전')\n" +
                "            when TIMESTAMPDIFF(DAY, DC.createdAt, current_timestamp())<7\n" +
                "            then concat(TIMESTAMPDIFF(DAY, DC.createdAt, current_timestamp()), '일 전')\n" +
                "            when TIMESTAMPDIFF(WEEK, DC.createdAt, current_timestamp())<5\n" +
                "            then concat(TIMESTAMPDIFF(WEEK, DC.createdAt, current_timestamp()), '주 전')\n" +
                "            else concat(TIMESTAMPDIFF(MONTH, DC.createdAt, current_timestamp()), '달 전')\n" +
                "         end as uploadTime\n" +
                "     , DC.content, juso.jusoName\n" +
                "from DealCom DC\n" +
                "    join User on DC.sendUserId = User.userId\n" +
                "    join (\n" +
                "        select User.userId, JusoCode.jusoName\n" +
                "        from UserArea\n" +
                "            inner join JusoCode on UserArea.jusoCodeId = JusoCode.jusoCodeId\n" +
                "            inner join User on UserArea.userId = User.userId\n" +
                "        where UserArea.status = 'Y'\n" +
                "    ) juso on juso.userId = User.userId\n" +
                "where receiveUserId = ?\n" +
                "order by DC.createdAt desc;";

        int Params = userId;

        return new GetUserInfoRes(
                getMyInfo = this.jdbcTemplate.queryForObject(Query,
                        (rs, rowNum) -> new GetMyInfo(
                                rs.getInt("userId"),
                                rs.getString("jusoName"),
                                rs.getString("userImg"),
                                rs.getString("name"),
                                rs.getDouble("score"),
                                rs.getInt("certificationCount"))
                        , Params),
                getMyCount = this.jdbcTemplate.queryForObject(Query1,
                        (rs, rowNum) -> new GetMyCount(
                                rs.getInt("postCount"),
                                rs.getInt("badgeCount"),
                                rs.getInt("comCount"))
                        , Params),
                getMyManners = this.jdbcTemplate.query(Query2,
                        (rs, rowNum) -> new GetMyManner(
                                rs.getString("manner"),
                                rs.getInt("mannerCount"))
                        , Params),
                getMyComments = this.jdbcTemplate.query(Query3,
                        (rs, rowNum) -> new GetMyComment(
                                rs.getInt("traderId"),
                                rs.getString("traderName"),
                                rs.getString("traderImg"),
                                rs.getString("uploadTime"),
                                rs.getString("content"),
                                rs.getString("jusoName"))
                        , Params)
        );

    }

    /**
     * 프로필 수정 API
     * [PATCH] /users/:userId
     *
     * @return BaseResponse<String>
     */
    public int modifyMyInfo(PatchMyInfoReq patchMyInfoReq) {
        String Query = "update User set userName=?, userProfileImageUrl=? where userId = ?;";
        Object[] Params = new Object[]{patchMyInfoReq.getName(), patchMyInfoReq.getUserImg(), patchMyInfoReq.getUserId()};
        return this.jdbcTemplate.update(Query, Params);
    }

    public int modifyMyArea(PatchMyInfoReq patchMyInfoReq) {
        String Query = "update UserArea set jusoCodeId=? where userId=?;";
        Object[] Params = new Object[]{patchMyInfoReq.getJusoCodeId(), patchMyInfoReq.getUserId()};
        return this.jdbcTemplate.update(Query, Params);
    }
    /**
     * 유저 배지 조회 API
     * [GET] /users/badge
     * @return BaseResponse<List<GetUserBadgeRes>>
     */
    public List<GetUserBadgeRes> getUserBadges(int userId) {
        String getUserBadgesQuery = "select Badge.badgeName, Badge.badgeImageUrl as badgeImage\n" +
                "from UserBadge U join Badge on Badge.badgeId = U.badgeId\n" +
                "where UserId = ?";
        int getUserBadgeParam = userId;
        return this.jdbcTemplate.query(getUserBadgesQuery,
                (rs, rowNum) -> new GetUserBadgeRes(
                        rs.getString("badgeName"),
                        rs.getString("badgeImage")),
                getUserBadgeParam);
    }

    /**
     * 유저 단골 가게 조회 API
     * [GET] /users/:userIdx/likestores
     * @return BaseResponse<List<GetUserLikeStoreRes>>
     */
    public List<GetUserLikeStoreRes> getUserLikeStores(int userId) {
        String getUserLikeStoresQuery = "select S.storeId, S.storeProfileImage as storeImage, S.storeName, S.storeInfo, categoryName.categoryName, RC.reviewCount, LU.likeUserCount\n" +
                "from Store S\n" +
                "    join LikeStoreList LSL on S.storeId = LSL.storeId\n" +
                "    join (\n" +
                "        select S.storeId, S.storeName, C.categoryName\n" +
                "        from Store S join Category C on S.storeCategoryId = C.categoryId\n" +
                "    ) categoryName on categoryName.storeId = S.storeId\n" +
                "    left join (\n" +
                "        select storeId, count(*) as reviewCount\n" +
                "        from StoreReview\n" +
                "        group by storeId\n" +
                "    ) RC on RC.storeId = S.storeId\n" +
                "    left join (\n" +
                "        select storeId, count(*) as likeUserCount\n" +
                "        from LikeStoreList\n" +
                "        group by storeId\n" +
                "    ) LU on LU.storeId = S.storeId\n" +
                "where LSL.userId = ?";

        int getUserLikeStoresParams = userId;
        return this.jdbcTemplate.query(getUserLikeStoresQuery,
                (rs, rowNum) -> new GetUserLikeStoreRes(
                        rs.getInt("storeId"),
                        rs.getString("storeImage"),
                        rs.getString("storeName"),
                        rs.getString("storeInfo"),
                        rs.getString("categoryName"),
                        rs.getInt("reviewCount"),
                        rs.getInt("likeUserCount")),
                getUserLikeStoresParams);
    }



    /**
     * 보유 쿠폰 상태별 조회 API
     * [GET] /users/:userId/coupons
     * [GET] /coupons?status=?
     * @return BaseResponse<List<GetUserCouponRes>>
     */
    public List<GetUserCouponRes> getCoupon(int userId, String status) {
        // 유효기간 남았을 경우
        String Query1 = "select Sinfo.storeProfileImage, Sinfo.storeName, Sinfo.categoryName, C.couponListId, C.couponName, DATE_FORMAT(C.endDate, '%Y년 %m월 %d일까지') AS endDate, '받은쿠폰' as time\n" +
                "from CouponList C join UserCoupon UC on C.couponListId = UC.couponListId\n" +
                "join (\n" +
                "    select S.storeId, S.storeProfileImage, S.storeName, categoryName.categoryName\n" +
                "    from Store S\n" +
                "        join (\n" +
                "            select S.storeId, S.storeName, C.categoryName\n" +
                "            from Store S join Category C on S.storeCategoryId = C.categoryId\n" +
                "        ) categoryName on categoryName.storeId = S.storeId\n" +
                "    ) Sinfo on Sinfo.storeId = C.storeId\n" +
                "where userId = ? and TIMESTAMPDIFF(SECOND, C.endDate, current_timestamp())<0;";

        // 유효기간 지났을 경우
        String Query2 = "select Sinfo.storeProfileImage, Sinfo.storeName, Sinfo.categoryName, C.couponListId, C.couponName, DATE_FORMAT(C.endDate, '%Y년 %m월 %d일까지') AS endDate, '기한만료' as time\n" +
                "from CouponList C join UserCoupon UC on C.couponListId = UC.couponListId\n" +
                "join (\n" +
                "    select S.storeId, S.storeProfileImage, S.storeName, categoryName.categoryName\n" +
                "    from Store S\n" +
                "        join (\n" +
                "            select S.storeId, S.storeName, C.categoryName\n" +
                "            from Store S join Category C on S.storeCategoryId = C.categoryId\n" +
                "        ) categoryName on categoryName.storeId = S.storeId\n" +
                "    ) Sinfo on Sinfo.storeId = C.storeId\n" +
                "where userId = ? and TIMESTAMPDIFF(SECOND, C.endDate, current_timestamp())>0;";

        int Params = userId;

        if (status == "Y"){
            return this.jdbcTemplate.query(Query1,
                    (rs,rowNum) -> new GetUserCouponRes(
                            rs.getString("storeProfileImage"),
                            rs.getString("storeName"),
                            rs.getString("categoryName"),
                            rs.getInt("couponListId"),
                            rs.getString("couponName"),
                            rs.getString("endDate"),
                            rs.getString("time")),
                    Params);
        } else {
            return this.jdbcTemplate.query(Query2,
                    (rs,rowNum) -> new GetUserCouponRes(
                            rs.getString("storeProfileImage"),
                            rs.getString("storeName"),
                            rs.getString("categoryName"),
                            rs.getInt("couponListId"),
                            rs.getString("couponName"),
                            rs.getString("endDate"),
                            rs.getString("time")),
                    Params);
        }
    }





    /**
     * 받은 매너 평가 조회 API
     * [GET] /users/:userIdx/manner
     * @return BaseResponse<List<GetUserMannerRes>>
     */
    public List<GetUserMannerRes> getUserManner(int userId) {
        String Query = "select UM.userId, ML.mannerContent as manner, count(UM.mannerListId) as count\n" +
                "from UserManner UM join MannerList ML on UM.mannerListId = ML.mannerListId\n" +
                "where UM.userId = ?\n" +
                "group by UM.mannerListId;";
        int Params = userId;

        return this.jdbcTemplate.query(Query,
                (rs, rowNum) -> new GetUserMannerRes(
                        rs.getInt("userId"),
                        rs.getString("manner"),
                        rs.getInt("count")),
                Params);

    }

    /**
     * 키워드 알림설정 API
     * [GET] /users/:userIdx/keywords
     * @return BaseResponse<String>
     */
    public int setKeyWords(int userId, String keyword) {
        String Query = "insert into KeywordList (userId, content) values (?,?);";
        Object[] Params = new Object[]{userId, keyword};

        this.jdbcTemplate.update(Query, Params);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);


    }

    // 키워드 확인
    public int checkKeyword(int userId, String keyword) {
        String Query = "select exists (select * from KeywordList where userId=? and content=? and status='Y');";
        Object[] Params = new Object[]{userId, keyword};
        int result = this.jdbcTemplate.queryForObject(
                Query, int.class, Params
        );
        System.out.println(result);
        return result;
    }


    /**
     * 키워드 알림설정 API
     * [POST] /users/:userIdx/keywords
     * @return BaseResponse<PostUserKeywordsRes>
     */
    public int createKeywords(int userId, String keyword) {
        String Query = "insert into KeywordList (userId, content) values(?,?);";
        Object[] Params = new Object[]{userId, keyword};
        this.jdbcTemplate.update(Query, Params);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }



    /**
     * 키워드 알림해제 API
     * [PATCH] /users/:userIdx/keywords
     * @return BaseResponse<String>
     */
    public int deleteKeywords(DeleteKeywordReq deleteKeywordReq) {
        String Query = "update KeywordList set status='N' where userId=? and content=?;";
        Object[] Params = new Object[]{deleteKeywordReq.getUserId(), deleteKeywordReq.getKeyword()};
        return this.jdbcTemplate.update(Query, Params);
    }

    /**
     * 관심 목록 전체 조회 API
     * [GET] /users/:userId/attention
     * @return BaseResponse<List<GetUserAttentionRes>>
     */
    public List<GetProductListRes> getAttention(int userId) {

        String Query = "select P.productPostId, img.firstImg, P.title, JusoCode.jusoName, P.price,\n" +
                "                       case\n" +
                "                           when TIMESTAMPDIFF(SECOND, P.createdAt, current_timestamp())<60\n" +
                "                            then concat(TIMESTAMPDIFF(SECOND, P.createdAt, current_timestamp()),'초 전')\n" +
                "                           when TIMESTAMPDIFF(MINUTE, P.createdAt, current_timestamp())<60\n" +
                "                            then concat(TIMESTAMPDIFF(MINUTE, P.createdAt, current_timestamp()),'분 전')\n" +
                "                            when TIMESTAMPDIFF(HOUR, P.createdAt, current_timestamp())<24\n" +
                "                            then concat(TIMESTAMPDIFF(HOUR, P.createdAt, current_timestamp()), '시간 전')\n" +
                "                            else concat(TIMESTAMPDIFF(DAY, P.createdAt, current_timestamp()), '일 전')\n" +
                "                        end as uploadTime\n" +
                "                       ,\n" +
                "                       case\n" +
                "                       when P.status = 'B'\n" +
                "                           then '예약중'\n" +
                "                        when P.status = 'C'\n" +
                "                            then '거래완료'\n" +
                "                        else '판매중'\n" +
                "                            end as state, attChat.chatCount, attChat.attCount\n" +
                "                from ProductPost P\n" +
                "                    left join JusoCode on P.jusoCodeId = JusoCode.jusoCodeId\n" +
                "                    left join (\n" +
                "                            select P.productPostId, count(PA.postId) as attCount, chat.chatCount\n" +
                "                            from ProductPost P\n" +
                "                                left join ProductAttention PA on PA.postId = P.productPostId\n" +
                "                                left join (\n" +
                "                                    select P.productPostId, count(PC.postId) div 2 as chatCount\n" +
                "                                    from ProductPost P\n" +
                "                                        left join ProductChatList PC on PC.postId = P.productPostId\n" +
                "                                group by P.productPostId) chat on chat.productPostId = P.productPostId where PA.status = 'Y'\n" +
                "                            group by P.productPostId) attChat on attChat.productPostId = P.productPostId\n" +
                "                    left join(\n" +
                "                        select P.productPostId, imageSelect.firstImg\n" +
                "                        from ProductPost P\n" +
                "                            left join(\n" +
                "                                select Img.productPostId, min(Img.productImageId), Img.imageUrl as firstImg\n" +
                "                                from ProductImage Img\n" +
                "                                group by Img.productPostId) imageSelect on imageSelect.productPostId = P.productPostId\n" +
                "                        ) img on img.productPostId = P.productPostId\n" +
                "                join (\n" +
                "                    select PA.postId\n" +
                "                    from ProductAttention PA\n" +
                "                    where userId = ?\n" +
                "                    ) att on att.postId = P.productPostId\n" +
                "                where P.isHidden = 'N' and P.isExistence = 'Y'\n" +
                "                order by P.createdAt DESC;";

        int Params = userId;
        return this.jdbcTemplate.query(Query,
                (rs,rowNum) -> new GetProductListRes(
                        rs.getString("firstImg"),
                        rs.getInt("productPostId"),
                        rs.getString("title"),
                        rs.getString("jusoName"),
                        rs.getInt("price"),
                        rs.getString("state"),
                        rs.getInt("chatCount"),
                        rs.getInt("attCount"),
                        rs.getString("uploadTime")),
                Params);

    }


    public char checkExistsUser(String encryptPhone) {
        String Query = "select status from User where UserPhoneNumber = ?;";
        String Params = encryptPhone;
        return this.jdbcTemplate.queryForObject(Query,
                char.class,
                Params);
    }

    /**
     * 회원 탈퇴 API
     * [PATCH] /users/:userId/deletion
     * @return BaseResponse<String>
     */
    public int deleteUser(PatchUserReq patchUserReq) {
        String Query = "update User set status=? where userId=?;";
        Object[] Params = new Object[]{patchUserReq.getStatus(), patchUserReq.getUserId()};
        return this.jdbcTemplate.update(Query, Params);

    }

    public int checkUser(int userId) {
        String Query = "select exists(select * from User where userId=?);";
        int Params = userId;
        return this.jdbcTemplate.queryForObject(Query,
                int.class,
                Params);
    }

    public String checkUserState(int userId) {
        String Query = "select status from User where userId=?;";
        int Params = userId;
        return this.jdbcTemplate.queryForObject(Query,
                String.class,
                Params);
    }

}
