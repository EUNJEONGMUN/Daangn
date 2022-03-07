package com.example.demo.src.store;

import com.example.demo.src.store.model.Req.PostNewsReq;
import com.example.demo.src.store.model.Req.PostStoreReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class StoreDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
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
                "    (userId, storeName, storeInfo, storePhoneNumber, storeCategoryId, storeJuso, storeSiteUrl, storeProfileImage)\n" +
                "values (?,?,?,?,?,?,?,?)";
        Object[] createStoreParams = new Object[]{postStoreReq.getUserId(), postStoreReq.getStoreName(), postStoreReq.getStoreInfo(),
                postStoreReq.getStorePhoneNumber(), postStoreReq.getStoreCategoryId(), postStoreReq.getJuso(),
                postStoreReq.getStoreSiteUrl(), postStoreReq.getStoreProfileImage()};

        this.jdbcTemplate.update(createStoreQuery, createStoreParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int checkTopCategory(int storeCategoryId) {
        String checkTopCategoryQuery = "select refId from Category where categoryId=?";
        int checkTopCategoryParam = storeCategoryId;
        return this.jdbcTemplate.queryForObject(checkTopCategoryQuery,
                int.class,
                checkTopCategoryParam);

    }

    public int checkStoreUser(int storeId, int userId) {
        String Query = "select exists (select * from Store where userId=? and storeId=?);";
        Object[] Params = new Object[]{userId, storeId};
        return this.jdbcTemplate.queryForObject(Query, int.class, Params);

    }

    public int modifyStore(int storeId, PostStoreReq postStoreReq) {
        String Query = "UPDATE Store " +
                "SET storeName=?, storeInfo=?, storePhoneNumber=?, storeCategoryId=?, " +
                "storeJuso=?, storeSiteUrl=?, storeProfileImage=? " +
                "where userId=? and storeId=?;";

        Object[] Params = new Object[]{postStoreReq.getStoreName(), postStoreReq.getStoreInfo(), postStoreReq.getStorePhoneNumber(),
                postStoreReq.getStoreCategoryId(), postStoreReq.getJuso(), postStoreReq.getStoreSiteUrl(), postStoreReq.getStoreProfileImage(),
                postStoreReq.getUserId(), storeId};

        return this.jdbcTemplate.update(Query, Params);


    }

    public int deleteStore(int storeId, int userId) {
        String Query = "DELETE from Store where storeId=? and userId=?;";
        Object[] Params = new Object[]{storeId, userId};
        return this.jdbcTemplate.update(Query, Params);
    }

    public int createNews(PostNewsReq postNewsReq) {
        String Query = "insert into StoreNews " +
                "(storeId, title, content) " +
                "values (?,?,?);";
        Object[] Params = new Object[]{postNewsReq.getStoreId(), postNewsReq.getTitle(), postNewsReq.getContent()};
        this.jdbcTemplate.update(Query, Params);

        String StoreNewsId = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(StoreNewsId, int.class);

    }

    public int modifyNews(int storeNewsId, PostNewsReq postNewsReq) {
        String Query = "update StoreNews set title=?, content=? where storeNewsId=? ans storeId=?;";

        Object[] Params = new Object[]{postNewsReq.getTitle(), postNewsReq.getContent(), storeNewsId, postNewsReq.getStoreId()};

        return this.jdbcTemplate.update(Query, Params);

    }
//
//    public int modifyNews(int postId, PostNewsReq postNewsReq) {
//        String Query = "update StoreNews set title=?, content=? where storeNewsId=? and storeId=?;";
//
//        Object[] Params = new Object[]{postNewsReq.getTitle(), postNewsReq.getContent(), postId, postNewsReq.getStoreId()};
//
//        return this.jdbcTemplate.update(Query, Params);
//
////    }
//    public int checkNewsUser(int postId, int storeId) {
//        // 전달받은 postId의 storeId와 body의 storeId와 같아야함
//        String Query = "select exists(select * from StoreNews SN where SN.storeNewsId =? and SN.storeId=?);";
//        Object[] Params = new Object[]{postId, storeId};
//        return this.jdbcTemplate.update(Query, Params);
//    }
}

