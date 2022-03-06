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

    /**
     * 홈 화면 조회 API
     * [GET] /products/home
     * @return BaseResponse<GetProductRes>
     */
    public List<GetProductRes> getProducts() {

        String Query = "select P.productPostId, img.firstImg, P.title, JusoCode.jusoName, P.price,\n" +
                "       case\n" +
                "           when TIMESTAMPDIFF(SECOND, P.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(SECOND, P.createdAt, current_timestamp()),'초 전')\n" +
                "           when TIMESTAMPDIFF(MINUTE, P.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(MINUTE, P.createdAt, current_timestamp()),'분 전')\n" +
                "            when TIMESTAMPDIFF(HOUR, P.createdAt, current_timestamp())<24\n" +
                "            then concat(TIMESTAMPDIFF(HOUR, P.createdAt, current_timestamp()), '시간 전')\n" +
                "            else concat(TIMESTAMPDIFF(DAY, P.createdAt, current_timestamp()), '일 전')\n" +
                "        end as uploadTime\n" +
                "       ,\n" +
                "       case\n" +
                "       when P.status = 'B'\n" +
                "           then '예약중'\n" +
                "        when P.status = 'C'\n" +
                "            then '거래완료'\n" +
                "        else '판매중'\n" +
                "            end as state, attChat.chatCount, attChat.attCount\n" +
                "from ProductPost P\n" +
                "    left join JusoCode on P.jusoCodeId = JusoCode.jusoCodeId\n" +
                "    left join (\n" +
                "            select P.productPostId, count(PA.postId) as attCount, chat.chatCount\n" +
                "            from ProductPost P\n" +
                "                left join ProductAttention PA on PA.postId = P.productPostId\n" +
                "                left join (\n" +
                "                    select P.productPostId, count(PC.postId) div 2 as chatCount\n" +
                "                    from ProductPost P\n" +
                "                        left join ProductChatList PC on PC.postId = P.productPostId\n" +
                "                group by P.productPostId) chat on chat.productPostId = P.productPostId where PA.status = 'Y'\n" +
                "            group by P.productPostId) attChat on attChat.productPostId = P.productPostId\n" +
                "    left join(\n" +
                "        select P.productPostId, imageSelect.firstImg\n" +
                "        from ProductPost P\n" +
                "            left join(\n" +
                "                select Img.productPostId, min(Img.productImageId), Img.imageUrl as firstImg\n" +
                "                from ProductImage Img\n" +
                "                group by Img.productPostId) imageSelect on imageSelect.productPostId = P.productPostId\n" +
                "        ) img on img.productPostId = P.productPostId\n" +
                "where P.isHidden = 'N' and P.isExistence = 'Y'\n" +
                "order by P.createdAt DESC;";


        return this.jdbcTemplate.query(Query,
                (rs,rowNum) -> new GetProductRes(
                        rs.getString("firstImg"),
                        rs.getInt("productPostId"),
                        rs.getString("title"),
                        rs.getString("jusoName"),
                        rs.getInt("price"),
                        rs.getString("state"),
                        rs.getInt("chatCount"),
                        rs.getInt("attCount"),
                        rs.getString("uploadTime"))
                );

    }

    /**
     * 홈 화면 카테고리별 조회 API
     * [GET] /products/home/:categoryId
     * @return BaseResponse<GetProductRes>
     */
    public List<GetProductRes> getProduct(int categoryId) {
        String Query = "select P.productPostId, img.firstImg, P.title, JusoCode.jusoName, P.price,\n" +
                "       case\n" +
                "           when TIMESTAMPDIFF(SECOND, P.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(SECOND, P.createdAt, current_timestamp()),'초 전')\n" +
                "           when TIMESTAMPDIFF(MINUTE, P.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(MINUTE, P.createdAt, current_timestamp()),'분 전')\n" +
                "            when TIMESTAMPDIFF(HOUR, P.createdAt, current_timestamp())<24\n" +
                "            then concat(TIMESTAMPDIFF(HOUR, P.createdAt, current_timestamp()), '시간 전')\n" +
                "            else concat(TIMESTAMPDIFF(DAY, P.createdAt, current_timestamp()), '일 전')\n" +
                "        end as uploadTime\n" +
                "       ,\n" +
                "       case\n" +
                "       when P.status = 'B'\n" +
                "           then '예약중'\n" +
                "        when P.status = 'C'\n" +
                "            then '거래완료'\n" +
                "        else '판매중'\n" +
                "            end as state, attChat.chatCount, attChat.attCount\n" +
                "from ProductPost P\n" +
                "    left join JusoCode on P.jusoCodeId = JusoCode.jusoCodeId\n" +
                "    left join (\n" +
                "            select P.productPostId, count(PA.postId) as attCount, chat.chatCount\n" +
                "            from ProductPost P\n" +
                "                left join ProductAttention PA on PA.postId = P.productPostId\n" +
                "                left join (\n" +
                "                    select P.productPostId, count(PC.postId) div 2 as chatCount\n" +
                "                    from ProductPost P\n" +
                "                        left join ProductChatList PC on PC.postId = P.productPostId\n" +
                "                group by P.productPostId) chat on chat.productPostId = P.productPostId where PA.status = 'Y'\n" +
                "            group by P.productPostId) attChat on attChat.productPostId = P.productPostId\n" +
                "    left join(\n" +
                "        select P.productPostId, imageSelect.firstImg\n" +
                "        from ProductPost P\n" +
                "            left join(\n" +
                "                select Img.productPostId, min(Img.productImageId), Img.imageUrl as firstImg\n" +
                "                from ProductImage Img\n" +
                "                group by Img.productPostId) imageSelect on imageSelect.productPostId = P.productPostId\n" +
                "        ) img on img.productPostId = P.productPostId\n" +
                "where P.isHidden = 'N' and P.isExistence = 'Y' and P.productPostCategoryId = ?\n" +
                "order by P.createdAt DESC;";

        int Params = categoryId;
        return this.jdbcTemplate.query(Query,
                (rs,rowNum) -> new GetProductRes(
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

    // 카테고리 범위 체크
    public int checkTopCategory(int categoryId) {
        String checkTopCategoryQuery = "select refId from Category where categoryId=?";
        int checkTopCategoryParam = categoryId;
        return this.jdbcTemplate.queryForObject(checkTopCategoryQuery,
                int.class,
                checkTopCategoryParam);
    }

    /**
     * 중고 거래 글 작성 API
     * [POST] /products/new
     * @return BaseResponse<PostProductNewRes>
     */
    public int createProduct(PostProductNewReq postProductNewReq) {
        String createProductQuery = "insert into ProductPost (userId, title, productPostCategoryId, price,content) VALUES(?,?,?,?,?)";
        Object[] createProductParams = new Object[]{postProductNewReq.getUserId(), postProductNewReq.getTitle(), postProductNewReq.getProductPostCategoryId(),
                                                    postProductNewReq.getPrice(), postProductNewReq.getContent()};
        this.jdbcTemplate.update(createProductQuery, createProductParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

    }

    /**
     * 중고 거래 글 수정 API
     * [PATCH] /products/:postId/:userId
     * @return BaseResponse<String>
     */
    public int modifyProduct(PatchPostReq patchPostReq) {
        String Query = "update ProductPost P " +
                "set P.title=?, P.productPostCategoryId=?, P.jusoCodeId=?, P.isProposal=?, P.content=?," +
                "P.price=?, P.status=? where P.userId=? and P.productPostId=?;";
        Object[] Params = new Object[]{patchPostReq.getTitle(), patchPostReq.getProductPostCategoryId(),
                patchPostReq.getProductPostLocation(), patchPostReq.getIsProposal(), patchPostReq.getContent(),
                patchPostReq.getPrice(), patchPostReq.getState(), patchPostReq.getUserId(), patchPostReq.getPostId()};

        return this.jdbcTemplate.update(Query, Params);
    }

    /**
     * 중고 거래 글 삭제 API
     * [PATCH] /products/:postId/:userId/status
     * @return BaseResponse<String>
     */
    public int deleteProduct(PatchPostStatusReq patchPostStatusReq) {
        String Query = "update ProductPost P " +
                "set P.isExistence=?" +
                "where P.userId=? and P.productPostId=?;";
        Object[] Params = new Object[]{patchPostStatusReq.getIsExistence(), patchPostStatusReq.getUserId(), patchPostStatusReq.getPostId()};
        return this.jdbcTemplate.update(Query, Params);
    }

    // 중고 거래 관심 등록 확인
    public int checkAtt(int postId, int userId) {
        String checkAttQuery = "select exists(select * from ProductAttention where postId=? and userId=?)";
        Object[] checkAttParams = new Object[]{postId, userId};

        return this.jdbcTemplate.queryForObject(checkAttQuery,
                int.class,
                checkAttParams);
    }

    /**
     * 중고 거래 글 관심 등록 API
     * [POST] /products/:postId/:userId/attention
     * @return BaseResponse<String>
     */
    public int createProductAtt(int postId, int userId, PostProductAttReq postProductAttReq) {

        String createProductAttQuery = "insert into ProductAttention (postId, userId, status) VALUES (?, ?, ?)";
        Object[] createProductAttParams = new Object[]{postId, userId, postProductAttReq.getStatus()};
        return this.jdbcTemplate.update(createProductAttQuery, createProductAttParams);
    }

    /**
     * 중고 거래 글 관심 변경 API
     * [PATCH] /products/:postId/:userId/attention
     * @return BaseResponse<String>
     */
    public int modifyProductAtt(PatchProductAttReq patchProductAttReq) {
        String Query = "UPDATE ProductAttention PA " +
                "set status=? " +
                "where postId=? and userId=?;";
        Object[] Params = new Object[]{patchProductAttReq.getStatus(), patchProductAttReq.getPostId(), patchProductAttReq.getUserId()};

        // toggle 형태
//        String Query = "UPDATE ProductAttention PA " +
//                "SET status = IF(status='Y', 'N', 'Y') " +
//                "where postId=? and userId=?;";
//        Object[] Params = new Object[]{patchProductAttReq.getPostId(), patchProductAttReq.getUserId()};

        return this.jdbcTemplate.update(Query, Params);

    }

    // 거래 기록 체크
    public int checkDeal(int postId, int userId) {
        String checkDealQuery = "select exists(select * from Deal where productPostId=? and buyUserId=?);";
        Object[] checkDealParams = new Object[]{postId, userId};

        return this.jdbcTemplate.queryForObject(checkDealQuery,
                int.class,
                checkDealParams);
    }

    /**
     * 중고 거래 성사 API
     * [POST] /products/:postId/:userId/deals
     * @return BaseResponse<PostDealRes>
     */
    public int createDeal(int postId, int userId, PostDealReq postDealReq) {
        String createDealQuery = "insert into Deal (productPostId, buyUserId, status) VALUES (?, ?, ?);";
        Object[] createDealParams = new Object[]{postId, userId, postDealReq.getStatus()};
        return this.jdbcTemplate.update(createDealQuery, createDealParams);

    }

    /**
     * 중고 거래 삭제 API
     * [PATCH] /products/:postId/:userId/status
     * @return BaseResponse<String>
     */
    public int deleteDeal(PatchDealReq patchDealReq) {
        String Query = "update Deal " +
                "set Deal.status=? " +
                "where Deal.productPostId=? and Deal.buyUserId=?;";
        Object[] Params = new Object[]{patchDealReq.getStatus(), patchDealReq.getPostId(), patchDealReq.getUserId()};

         //toggle 형태
//        String Query = "UPDATE Deal " +
//                "SET status = IF(status='Y', 'N', 'Y') " +
//                "where productPostId=? and buyUserId=?;";
//        Object[] Params = new Object[]{patchDealReq.getPostId(), patchDealReq.getUserId()};

        return this.jdbcTemplate.update(Query, Params);

    }


    /**
     * 판매 내역 상태별 조회 API
     * [GET] /products/user-post/:userId
     * /:userId?status=?
     * @return BaseResponse<List<GetProductRes>>
     */
    public List<GetProductRes> getUserProductPost(int userId, int status) {

        // 판매중
        String Query1 = "select P.productPostId, img.firstImg, P.title, JusoCode.jusoName, P.price,\n" +
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
                "                           then '거래완료'\n" +
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
                "                               group by P.productPostId) chat on chat.productPostId = P.productPostId where PA.status = 'Y'\n" +
                "                            group by P.productPostId) attChat on attChat.productPostId = P.productPostId\n" +
                "                    left join(\n" +
                "                        select P.productPostId, imageSelect.firstImg\n" +
                "                        from ProductPost P\n" +
                "                            left join(\n" +
                "                                select Img.productPostId, min(Img.productImageId), Img.imageUrl as firstImg\n" +
                "                                from ProductImage Img\n" +
                "                                group by Img.productPostId) imageSelect on imageSelect.productPostId = P.productPostId\n" +
                "                        ) img on img.productPostId = P.productPostId\n" +
                "                where P.isHidden = 'N' and P.isExistence = 'Y' and (P.status='A' or P.status='B') and P.userId = ?\n" +
                "                order by P.createdAt DESC;";
        // 거래완료
        String Query2 = "select P.productPostId, img.firstImg, P.title, JusoCode.jusoName, P.price,\n" +
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
                "                           then '거래완료'\n" +
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
                "                               group by P.productPostId) chat on chat.productPostId = P.productPostId where PA.status = 'Y'\n" +
                "                            group by P.productPostId) attChat on attChat.productPostId = P.productPostId\n" +
                "                    left join(\n" +
                "                        select P.productPostId, imageSelect.firstImg\n" +
                "                        from ProductPost P\n" +
                "                            left join(\n" +
                "                                select Img.productPostId, min(Img.productImageId), Img.imageUrl as firstImg\n" +
                "                                from ProductImage Img\n" +
                "                                group by Img.productPostId) imageSelect on imageSelect.productPostId = P.productPostId\n" +
                "                        ) img on img.productPostId = P.productPostId\n" +
                "                where P.isHidden = 'N' and P.isExistence = 'Y' and P.status='C' and P.userId = ?\n" +
                "                order by P.createdAt DESC;\n";
        // 숨김
        String Query3 = "select P.productPostId, img.firstImg, P.title, JusoCode.jusoName, P.price,\n" +
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
                "                           then '거래완료'\n" +
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
                "                               group by P.productPostId) chat on chat.productPostId = P.productPostId where PA.status = 'Y'\n" +
                "                            group by P.productPostId) attChat on attChat.productPostId = P.productPostId\n" +
                "                    left join(\n" +
                "                        select P.productPostId, imageSelect.firstImg\n" +
                "                        from ProductPost P\n" +
                "                            left join(\n" +
                "                                select Img.productPostId, min(Img.productImageId), Img.imageUrl as firstImg\n" +
                "                                from ProductImage Img\n" +
                "                                group by Img.productPostId) imageSelect on imageSelect.productPostId = P.productPostId\n" +
                "                        ) img on img.productPostId = P.productPostId\n" +
                "                where P.isHidden = 'Y' and P.isExistence = 'Y' and P.userId = ?\n" +
                "                order by P.createdAt DESC;";

        int Params = userId;

        if (status == 2){
            return this.jdbcTemplate.query(Query2,
                    (rs,rowNum) -> new GetProductRes(
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
        } else if (status == 3){
            return this.jdbcTemplate.query(Query3,
                    (rs,rowNum) -> new GetProductRes(
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
        } else {
            return this.jdbcTemplate.query(Query1,
                    (rs,rowNum) -> new GetProductRes(
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
    }
}
