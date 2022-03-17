package com.example.demo.src.product;

import com.example.demo.src.product.model.*;
import com.example.demo.src.product.model.Req.*;
import com.example.demo.src.product.model.Res.GetProductListRes;
import com.example.demo.src.product.model.Res.GetProductPostRes;
import com.example.demo.src.product.model.Res.GetProductRes;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Repository
public class ProductDao {

    private JdbcTemplate jdbcTemplate;
    private List<GetProductInfo> getProductInfo;
    private List<GetProductImg> getProductImg;
    private List<GetProductAttCount> getProductAttCount;
    private List<GetProductChatCount> getProductChatCount;
    private List<GetProductJuso> getProductJuso;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 홈 화면 조회 API
     * [GET] /products/home
     * @return BaseResponse<List<GetProductRes>>
     */
    public List<GetProductListRes> getProducts() {

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
                (rs,rowNum) -> new GetProductListRes(
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
//    public List<GetProductPostRes> getProducts() {
//
//        String Query1 = "SELECT P.productPostId, P.title,P.price, P.userId,\n" +
//                "       case\n" +
//                "            when TIMESTAMPDIFF(SECOND, P.createdAt, current_timestamp())<60\n" +
//                "                then concat(TIMESTAMPDIFF(SECOND, P.createdAt, current_timestamp()),'초 전')\n" +
//                "            when TIMESTAMPDIFF(MINUTE, P.createdAt, current_timestamp())<60\n" +
//                "                then concat(TIMESTAMPDIFF(MINUTE, P.createdAt, current_timestamp()),'분 전')\n" +
//                "            when TIMESTAMPDIFF(HOUR, P.createdAt, current_timestamp())<24\n" +
//                "                then concat(TIMESTAMPDIFF(HOUR, P.createdAt, current_timestamp()), '시간 전')\n" +
//                "            else concat(TIMESTAMPDIFF(DAY, P.createdAt, current_timestamp()), '일 전')\n" +
//                "            end as uploadTime ,\n" +
//                "        case\n" +
//                "            when P.status = 'B'\n" +
//                "                then '예약중'\n" +
//                "            when P.status = 'C'\n" +
//                "                then '거래완료'\n" +
//                "            else '판매중'\n" +
//                "            end as state\n" +
//                "FROM ProductPost P\n" +
//                "WHERE P.isExistence='Y' and P.isHidden='N'\n" +
//                "ORDER BY P.createdAt DESC;";
//
//        String Query2 = "SELECT P.productPostId, Image.firstImg\n" +
//                "FROM ProductPost P LEFT JOIN (SELECT Img.productPostId, min(Img.productImageId), Img.imageUrl as firstImg\n" +
//                "FROM ProductImage Img JOIN ProductPost PP on Img.productPostId = PP.productPostId\n" +
//                "GROUP BY Img.productPostId) Image ON Image.productPostId = P.productPostId\n" +
//                "WHERE P.isExistence='Y' and P.isHidden='N'\n" +
//                "ORDER BY P.createdAt DESC;";
//
//        String Query3 = "SELECT P.productPostId, IFNULL(Att.attCount, 0) as attCount\n" +
//                "FROM ProductPost P LEFT JOIN (SELECT PA.postId, COUNT(*) AS attCount\n" +
//                "FROM ProductAttention PA\n" +
//                "GROUP BY PA.postId) Att ON Att.postId = P.productPostId\n" +
//                "WHERE P.isExistence='Y' and P.isHidden='N'\n" +
//                "ORDER BY P.createdAt DESC;";
//
//        String Query4 = "SELECT P.productPostId, IFNULL(Chat.chatCount, 0) as chatCount\n" +
//                "FROM ProductPost P LEFT JOIN (SELECT PCL.postId, COUNT(*) AS chatCount\n" +
//                "FROM ProductChatList PCL\n" +
//                "GROUP BY PCL.postId) Chat ON Chat.postId = P.productPostId\n" +
//                "WHERE P.isExistence='Y' and P.isHidden='N'\n" +
//                "ORDER BY P.createdAt DESC;";
//
//        String Query5 = "SELECT P.productPostId, Juso.jusoName\n" +
//                "FROM ProductPost P JOIN (SELECT UA.userId, JC.jusoName\n" +
//                "    FROM UserArea UA\n" +
//                "    JOIN JusoCode JC on UA.jusoCodeId = JC.jusoCodeId) Juso ON Juso.userId=P.userId\n" +
//                "WHERE P.isExistence='Y' and P.isHidden='N'\n" +
//                "ORDER BY P.createdAt DESC;";
//
//        List<String> Image = this.jdbcTemplate.query(Query2,
//                (rs, rowNum) -> {
//                    String image = rs.getString("firstImg");
//                    return image;});
//
//        List<Integer> Att = this.jdbcTemplate.query(Query3,
//                (rs, rowNum) -> {
//                    int att = rs.getInt("attCount");
//                    return att;});
//
//        List<Integer> Chat = this.jdbcTemplate.query(Query4,
//                (rs, rowNum) -> {
//                    int chat = rs.getInt("chatCount");
//                    return chat;});
//
//        List<String> Juso = this.jdbcTemplate.query(Query5,
//                (rs, rowNum) -> {
//                    String juso = rs.getString("jusoName");
//                    return juso;});
//
//        List<GetProductInfo> Info = this.jdbcTemplate.query(Query1,
//                (rs,rowNum) -> {
//                    String title = rs.getString("title");
//                    int price = rs.getInt("price");
//                    String state = rs.getString("state");
//                    String uploadTime = rs.getString("uploadTime");
//                    return new GetProductInfo(title, price, state, uploadTime);
//                });
//
//        List<GetProductPostRes> getProductPostRes = (List<GetProductPostRes>) new GetProductPostRes();
//        for (int i=0; i<Image.size(); i++){
//            GetProductInfo getProductInfo = Info.get(i);
//            String firstImg = Image.get(i);
//            String jusoName = Juso.get(i);
//            int attCount = Att.get(i);
//            int chatCount = Chat.get(i);
//
//            //...?
//        }
//
//
//        return this.jdbcTemplate.query(Query1,
//                (rs, rowNum) -> {
//            String title = rs.getString("title");
//            int price = rs.getInt("price");
//            String state = rs.getString("state");
//            String uploadTime = rs.getString("uploadTime");
//            return new GetProductPostRes(new GetProductInfo(title, price, state, uploadTime), Image, Att, Chat, Juso);
//                });
//    }




    /**
     * 홈 화면 카테고리별 조회 API
     * [GET] /products/home/:categoryId
     * @return BaseResponse<List<GetProductRes>>
     */
    public List<GetProductListRes> getProduct(int categoryId) {
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





    /**
     * 중고 거래 글 작성 API
     * [POST] /products/new
     * @return BaseResponse<PostProductNewRes>
     */
    public int createProduct(int userJusoCodeId, PostProductNewReq postProductNewReq) {
        String createProductQuery = "insert into ProductPost (userId, title, productPostCategoryId, isProposal, content, price, jusoCodeId) VALUES(?,?,?,?,?,?,?)";
        Object[] createProductParams = new Object[]{postProductNewReq.getUserId(), postProductNewReq.getTitle(), postProductNewReq.getCategoryId(), postProductNewReq.getIsProposal(),
                                                    postProductNewReq.getContent(), postProductNewReq.getPrice(), userJusoCodeId};
        this.jdbcTemplate.update(createProductQuery, createProductParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

    }


    /**
     * 중고 거래 글 수정 API
     * [PATCH] /products/:postId
     * @return BaseResponse<String>
     */
    public int modifyProduct(PatchPostReq patchPostReq) {
        String Query = "update ProductPost P " +
                "set P.title=?, P.productPostCategoryId=?, P.jusoCodeId=?, P.isProposal=?, P.content=?," +
                "P.price=?, P.status=?, P.isHidden=?, P.isExistence=? where P.userId=? and P.productPostId=?;";
        Object[] Params = new Object[]{patchPostReq.getTitle(), patchPostReq.getProductPostCategoryId(),
                patchPostReq.getProductPostLocation(), patchPostReq.getIsProposal(), patchPostReq.getContent(),
                patchPostReq.getPrice(), patchPostReq.getStatus(), patchPostReq.getIsHidden(),
                patchPostReq.getIsExistence(), patchPostReq.getUserId(), patchPostReq.getPostId()};

        return this.jdbcTemplate.update(Query, Params);
    }


    /**
     * 중고 거래 글 관심 등록 API
     * [POST] /products/:postId/attention
     * @return BaseResponse<String>
     */
    public int createProductAtt(int postId, int userId, String status) {

        String createProductAttQuery = "insert into ProductAttention (postId, userId, status) VALUES (?, ?, ?)";
        Object[] createProductAttParams = new Object[]{postId, userId, status};
        return this.jdbcTemplate.update(createProductAttQuery, createProductAttParams);
    }

    /**
     * 중고 거래 글 관심 변경 API
     * [PATCH] /products/:postId/attention
     * @return BaseResponse<String>
     */
    public int modifyProductAtt(PatchProductAttReq patchProductAttReq) {
        String Query = "UPDATE ProductAttention PA " +
                "set status=? " +
                "where postId=? and userId=?;";
        Object[] Params = new Object[]{patchProductAttReq.getStatus(), patchProductAttReq.getPostId(), patchProductAttReq.getUserId()};

        return this.jdbcTemplate.update(Query, Params);

    }


    /**
     * 중고 거래 성사 API
     * [POST] /products/:postId/deals
     * @return BaseResponse<String>
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
        return this.jdbcTemplate.update(Query, Params);

    }


    /**
     * 판매 내역 상태별 조회 API
     * [GET] /products/user-post
     * /user-post?status=?
     * @return BaseResponse<List<GetProductRes>>
     */
    public List<GetProductListRes> getUserProductPost(int userId, String status) {

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

        if (status.equals("finish")){
            return this.jdbcTemplate.query(Query2,
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
        if (status.equals("hidden")){
            return this.jdbcTemplate.query(Query3,
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

        return this.jdbcTemplate.query(Query1,
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

    /**
     * 구매 내역 조회 API
     * [GET] /products/buylist
     * @return BaseResponse<List<GetProductRes>>
     */
    public List<GetProductListRes> getUserBuyList(int userId) {
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
                "                join (\n" +
                "                    select productPostId from Deal where buyUserId = ?\n" +
                "                    ) buy on buy.productPostId = P.productPostId\n" +
                "                where P.isExistence = 'Y'\n" +
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


    /**
     * 중고 거래 글 조회 API
     * [GET] /products/:postId
     * @return BaseResponse<GetProductRes>
     */
    public GetProductRes getPost(int postId) {

        String Query = "select P.productPostId, img.firstImg, P.title, JusoCode.jusoName, P.price, P.content,\n" +
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
                "where P.isHidden = 'N' and P.isExistence = 'Y' and P.productPostId=?\n" +
                "order by P.createdAt DESC;";

        int Params = postId;
        return this.jdbcTemplate.queryForObject(Query,
                (rs,rowNum) -> new GetProductRes(
                        rs.getString("firstImg"),
                        rs.getInt("productPostId"),
                        rs.getString("title"),
                        rs.getString("jusoName"),
                        rs.getInt("price"),
                        rs.getString("state"),
                        rs.getInt("chatCount"),
                        rs.getInt("attCount"),
                        rs.getString("uploadTime"),
                        rs.getString("content")),
                        Params);

    }

    // 중고 거래 관심 등록 확인
    public int checkAtt(int postId, int userId) {
        String checkAttQuery = "select exists(select * from ProductAttention where postId=? and userId=?)";
        Object[] checkAttParams = new Object[]{postId, userId};

        return this.jdbcTemplate.queryForObject(checkAttQuery,
                int.class,
                checkAttParams);
    }
    // 사용자 관심 등록 확인
    public String checkAttStatus(int postId, int userId) {
        String checkAttQuery = "select status from ProductAttention where postId=? and userId=?";
        Object[] checkAttParams = new Object[]{postId, userId};

        return this.jdbcTemplate.queryForObject(checkAttQuery,
                String.class,
                checkAttParams);
    }

    // 거래 기록 체크
    public int checkDeal(int postId) {
        String checkDealQuery = "select exists(select * from Deal where productPostId=? and status='Y');";
        int Params = postId;

        return this.jdbcTemplate.queryForObject(checkDealQuery,
                int.class,
                Params);
    }
    // 사용자의 거래기록 상태 체크
    public String checkDealUser(int postId, int userId) {
        String checkDealQuery = "select status from Deal where productPostId=? and buyUserId=?;";
        Object[] Params = new Object[]{postId, userId};
        return this.jdbcTemplate.queryForObject(checkDealQuery,
                String.class,
                Params);
    }

    // 사용자 주소 코드 찾기
    public int findUserJusoCodeId(int userId) {
        String Query = "select jusoCodeId from UserArea where userId=?;";
        int Params = userId;
        return this.jdbcTemplate.queryForObject(Query,
                int.class,
                Params);

    }

    // 게시글 존재 확인
    public int checkPostExists(int postId) {
        String Query = "select exists(select * from ProductPost where productPostId=? and isExistence='Y');";
        int Params = postId;
        int result = this.jdbcTemplate.queryForObject(Query,
                int.class,
                Params);

        System.out.println(result);
        return result;
    }

    // 상위 카테고리 체크
    public int checkTopCategory(int categoryId) {
        String Query = "select refId from Category where categoryId=?";
        int Param = categoryId;
        return this.jdbcTemplate.queryForObject(Query,
                int.class,
                Param);
    }


}
