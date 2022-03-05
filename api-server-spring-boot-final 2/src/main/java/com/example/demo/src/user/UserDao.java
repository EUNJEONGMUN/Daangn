package com.example.demo.src.user;

import com.example.demo.src.user.model.*;
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
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

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
        String Query = "select exists (select * from KeywordList where userId=? and content=?);";
        Object[] Params = new Object[]{userId, keyword};
        return this.jdbcTemplate.queryForObject(
                Query, int.class, Params
        );
    }

    /**
     * 키워드 알림설정 API
     * [PUT] /users/:userIdx/keywords
     * @return BaseResponse<String>
     */
    public int modifyKeywords(int userId, String keyword) {
        String Query = "UPDATE KeywordList SET status = IF(status='Y', 'N', 'Y') where userId=? and content=?;";
        Object[] Params = new Object[]{userId, keyword};
        this.jdbcTemplate.update(Query, Params);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    /**
     * 유저 개인 정보 조회 API
     * [GET] /users/:userId
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
        String Query2= "select ML.mannerContent as manner, count(UM.mannerListId) as mannerCount\n" +
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
                        ,Params),
                getMyCount = this.jdbcTemplate.queryForObject(Query1,
                        (rs, rowNum) -> new GetMyCount(
                                rs.getInt("postCount"),
                                rs.getInt("badgeCount"),
                                rs.getInt("comCount"))
                        ,Params),
                getMyManners = this.jdbcTemplate.query(Query2,
                        (rs, rowNum) -> new GetMyManner(
                                rs.getString("manner"),
                                rs.getInt("mannerCount"))
                        ,Params),
                getMyComments = this.jdbcTemplate.query(Query3,
                        (rs, rowNum) -> new GetMyComment(
                                rs.getInt("traderId"),
                                rs.getString("traderName"),
                                rs.getString("traderImg"),
                                rs.getString("uploadTime"),
                                rs.getString("content"),
                                rs.getString("jusoName"))
                        ,Params)
        );

    }

    /**
     * 유저 개인 정보 수정 API
     * [PATCH] /users/:userId
     * @return BaseResponse<String>
     */
    public int modifyMyInfo(PatchMyInfoReq patchMyInfoReq) {
        String Query = "update User set userName=?, userProfileImageUrl=? where userId = ?;";
        Object[] Params = new Object[]{patchMyInfoReq.getName(), patchMyInfoReq.getUserImg(), patchMyInfoReq.getUserId()};
        return this.jdbcTemplate.update(Query, Params);
    }

    /**
     * 관심 목록 전체 조회 API
     * [GET] /users/:userId/attention
     * @return BaseResponse<>
     */

    /**
     * 관심 목록 카테고리별 조회 API
     * [GET] /users/:userId/attention
     * [GET] /attention?categoryId=?
     * @return BaseResponse<>
     */

    /**
     * 보유 쿠폰 상태별 조회 API
     * [GET] /users/:userId/coupons
     * [GET] /coupons?status=?
     * @return BaseResponse<>
     */

}
