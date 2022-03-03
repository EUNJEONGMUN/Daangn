package com.example.demo.src.user;

import com.example.demo.src.user.model.GetUserBadgeRes;
import com.example.demo.src.user.model.GetUserLikeStoreRes;
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


}
