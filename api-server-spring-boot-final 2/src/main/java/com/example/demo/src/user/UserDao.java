package com.example.demo.src.user;

import com.example.demo.src.user.model.GetUserBadgeRes;
import com.example.demo.src.user.model.GetUserLikeStoreRes;
import com.example.demo.src.user.model.GetUserMannerRes;
import com.example.demo.src.user.model.GetUserSalesListRes;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {
    private JdbcTemplate jdbcTemplate;

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

    public int setKeyWords(int userId, String keyword) {
        String Query = "insert into KeywordList (userId, content) values (?,?);";
        Object[] Params = new Object[]{userId, keyword};

        this.jdbcTemplate.update(Query, Params);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);


    }

    public int checkKeyword(int userId, String keyword) {
        String Query = "select exists (select * from KeywordList where userId=? and content=?);";
        Object[] Params = new Object[]{userId, keyword};
        return this.jdbcTemplate.queryForObject(
                Query, int.class, Params
        );
    }

    public int modifyKeywords(int userId, String keyword) {
        String Query = "UPDATE KeywordList SET status = IF(status='Y', 'N', 'Y') where userId=? and content=?;";
        Object[] Params = new Object[]{userId, keyword};
        this.jdbcTemplate.update(Query, Params);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
}
