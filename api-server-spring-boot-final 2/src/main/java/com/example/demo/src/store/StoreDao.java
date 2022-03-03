package com.example.demo.src.store;

import com.example.demo.src.store.model.PostStoreReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class StoreDao {

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int checkStoreCount(int userId) {
        String checkStoreCountQuery = "select count(*) from Store where userId = ?";
        int checkStoreCountParam = userId;
        return this.jdbcTemplate.queryForObject(checkStoreCountQuery,
                int.class,
                checkStoreCountParam);

    }

    public int createStore(PostStoreReq postStoreReq) {
        String createStoreQuery = "insert into Store\n" +
                "    (userId, storeName, storeInfo, storePhoneNumber, storeCategoryId, storeLocationId, storeSiteUrl, storeProfileImage)\n" +
                "values (?,?,?,?,?,?,?,?)";
        Object[] createStoreParams = new Object[]{postStoreReq.getUserId(), postStoreReq.getStoreName(), postStoreReq.getStoreInfo(),
        postStoreReq.getStorePhoneNumber(), postStoreReq.getStoreCategoryId(), postStoreReq.getStoreLocationId(),
        postStoreReq.getStoreSiteUrl(), postStoreReq.getStoreProfileImage()};

        this.jdbcTemplate.update(createStoreQuery, createStoreParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int checkTopCategory(int storeCategoryId) {
        String checkTopCategoryQuery = "select refId from Category where categoryId=?";
        int checkTopCategoryParam = storeCategoryId;
        return this.jdbcTemplate.queryForObject(checkTopCategoryQuery,
                int.class,
                checkTopCategoryParam);

    }
}
