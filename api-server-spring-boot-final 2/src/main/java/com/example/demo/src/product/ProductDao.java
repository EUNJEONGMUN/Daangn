package com.example.demo.src.product;

import com.example.demo.src.product.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }





    public List<GetProductRes> getProducts() {
        String getProductsQuery = "select P.productPostId as postId,P.title as title, JusoCode.읍면동명 as juso, P.price as price,\n" +
                "       postImg.firstImage as firstImg,chatAtten.attCount as attCount,chatAtten.chatCount as chatCount,\n" +
                "       case\n" +
                "       when P.status = 'B'\n" +
                "           then '예약중'\n" +
                "        when P.status = 'C'\n" +
                "            then '거래완료'\n" +
                "        else '미선택'\n" +
                "            end as status\n" +
                "from ProductPost P\n" +
                "    left join JusoCode on P.jusoCodeId = JusoCode.jusoCodeId\n" +
                "    join (\n" +
                "        select P.productPostId, imageSelect.firstImage\n" +
                "        from ProductPost P\n" +
                "            left join(\n" +
                "                    select Img.productPostId, min(Img.productImageId), Img.imageUrl as firstImage\n" +
                "                    from ProductImage Img\n" +
                "                    group by Img.productPostId\n" +
                "            ) imageSelect on imageSelect.productPostId = P.productPostId\n" +
                "        ) postImg on postImg.productPostId = P.productPostId\n" +
                "    join (\n" +
                "        select P.productPostId, count(ChatList.postId) div 2 as chatCount, attention.attCount\n" +
                "        from ProductPost P\n" +
                "            left join ProductChatList ChatList on ChatList.postId = P.productPostId\n" +
                "            join (\n" +
                "                select P.productPostId, count(PA.postId) as attCount\n" +
                "                from ProductPost P\n" +
                "                    left join ProductAttention PA on PA.postId = P.productPostId\n" +
                "                group by P.productPostId\n" +
                "            ) attention on attention.productPostId = P.productPostId\n" +
                "        group by P.productPostId\n" +
                "        ) chatAtten on chatAtten.productPostId = P.productPostId\n" +
                "where P.isHidden = 'N'\n" +
                "order by P.createdAt desc";

        return this.jdbcTemplate.query(
                getProductsQuery,
                (rs, rowNum) -> new GetProductRes(
                        rs.getInt("postId"),
                        rs.getString("title"),
                        rs.getString("juso"),
                        rs.getInt("price"),
                        rs.getString("firstImg"),
                        rs.getInt("attCount"),
                        rs.getInt("chatCount"),
                        rs.getString("status")
                        )
                );




    }

    public List<GetProductRes> getProduct(int categoryId) {
        String getProductQuery = "select P.productPostId as postId,P.title as title, JusoCode.읍면동명 as juso, P.price as price,\n" +
                "       postImg.firstImage as firstImg,chatAtten.attCount as attCount,chatAtten.chatCount as chatCount,\n" +
                "       case\n" +
                "       when P.status = 'B'\n" +
                "           then '예약중'\n" +
                "        when P.status = 'C'\n" +
                "            then '거래완료'\n" +
                "        else '미선택'\n" +
                "            end as status\n" +
                "from ProductPost P\n" +
                "    left join JusoCode on P.jusoCodeId = JusoCode.jusoCodeId\n" +
                "    join (\n" +
                "        select P.productPostId, imageSelect.firstImage\n" +
                "        from ProductPost P\n" +
                "            left join(\n" +
                "                    select Img.productPostId, min(Img.productImageId), Img.imageUrl as firstImage\n" +
                "                    from ProductImage Img\n" +
                "                    group by Img.productPostId\n" +
                "            ) imageSelect on imageSelect.productPostId = P.productPostId\n" +
                "        ) postImg on postImg.productPostId = P.productPostId\n" +
                "    join (\n" +
                "        select P.productPostId, count(ChatList.postId) div 2 as chatCount, attention.attCount\n" +
                "        from ProductPost P\n" +
                "            left join ProductChatList ChatList on ChatList.postId = P.productPostId\n" +
                "            join (\n" +
                "                select P.productPostId, count(PA.postId) as attCount\n" +
                "                from ProductPost P\n" +
                "                    left join ProductAttention PA on PA.postId = P.productPostId\n" +
                "                group by P.productPostId\n" +
                "            ) attention on attention.productPostId = P.productPostId\n" +
                "        group by P.productPostId\n" +
                "        ) chatAtten on chatAtten.productPostId = P.productPostId\n" +
                "where P.isHidden = 'N' and P.productPostCategoryId = ?\n" +
                "order by P.createdAt desc";
        int getProductParams = categoryId;
        return this.jdbcTemplate.query(
                getProductQuery,
                (rs, rowNum) -> new GetProductRes(
                        rs.getInt("postId"),
                        rs.getString("title"),
                        rs.getString("juso"),
                        rs.getInt("price"),
                        rs.getString("firstImg"),
                        rs.getInt("attCount"),
                        rs.getInt("chatCount"),
                        rs.getString("status")
                ),
                getProductParams);
    }

    public int createProduct(PostProductNewReq postProductNewReq) {
        String createProductQuery = "insert into ProductPost (userId, title, productPostCategoryId, price,content) VALUES(?,?,?,?,?)";
        Object[] createProductParams = new Object[]{postProductNewReq.getUserId(), postProductNewReq.getTitle(), postProductNewReq.getProductPostCategoryId(),
                                                    postProductNewReq.getPrice(), postProductNewReq.getContent()};
        this.jdbcTemplate.update(createProductQuery, createProductParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

    }

    public int createProductFree(PostProductNewReq postProductNewReq) {
        String createProductQuery = "insert into ProductPost (userId, title, productPostCategoryId,content) VALUES(?,?,?,?)";
        Object[] createProductParams = new Object[]{postProductNewReq.getUserId(), postProductNewReq.getTitle(), postProductNewReq.getProductPostCategoryId(),
                postProductNewReq.getContent()};
        this.jdbcTemplate.update(createProductQuery, createProductParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

    }

    public int checkAtt(int postId, int userId) {
        String checkAttQuery = "select exists(select * from ProductAttention where postId=? and userId=?)";
        Object[] checkAttParams = new Object[]{postId, userId};

        int result = this.jdbcTemplate.queryForObject(checkAttQuery,
                int.class,
                checkAttParams);

        if (result == 0){
            return result; // 0 -> 존재하지 않음
        } else {
            String checkAttStatusQuery = "select status from ProductAttention where postId=? and userId=?";
            Object[] checkAttStatusParams = new Object[]{postId, userId};
            return this.jdbcTemplate.queryForObject(checkAttStatusQuery,
                    char.class,
                    checkAttStatusParams);
        }

    }

    public int createProductAtt(int postId, PutProductAttReq putProductAttReq) {

        String createProductAttQuery = "insert into ProductAttention (postId, userId) VALUES (?, ?)";
        Object[] createProductAttParams = new Object[]{postId, putProductAttReq.getUserId()};
        return this.jdbcTemplate.update(createProductAttQuery, createProductAttParams);


    }

    public int modifyProductAtt(int postId, PutProductAttReq putProductAttReq) {

        String modifyProductAttQuery = "UPDATE ProductAttention SET status = IF(status='Y', 'N', 'Y') where postId=? and userId=?";
        Object[] modifyProductAttParams = new Object[]{postId, putProductAttReq.getUserId()};
        return this.jdbcTemplate.update(modifyProductAttQuery, modifyProductAttParams);

    }

    public int createDeal(int postId, PostDealReq postDealReq) {
        String createDealQuery = "insert into Deal (buyUserId, productPostId) VALUES (?, ?)";
        Object[] createDealParams = new Object[]{postDealReq.getUserId(), postId};
        return this.jdbcTemplate.update(createDealQuery, createDealParams);
    }

    public int checkDeal(int postId, int userId) {
        String checkDealQuery = "select exists(select * from Deal where productPostId=? and buyUserId=?);";
        Object[] checkDealParams = new Object[]{postId, userId};
        return this.jdbcTemplate.update(checkDealQuery, checkDealParams);
    }

    public int deleteDeal(int postId, int userId) {
        String deleteDealQuery = "delete from Deal where productPostId=? and buyUserId=?";
        Object[] deleteDealParams = new Object[]{postId, userId};
        return this.jdbcTemplate.update(deleteDealQuery, deleteDealParams);
    }
}
