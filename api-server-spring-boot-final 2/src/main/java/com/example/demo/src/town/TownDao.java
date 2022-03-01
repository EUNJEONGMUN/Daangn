package com.example.demo.src.town;

import com.example.demo.src.town.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class TownDao {

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetTownRes> getTowns(){
        String getTownsQuery = "select * from TownPost";
        return this.jdbcTemplate.query(
                getTownsQuery,
                (rs, rowNum) -> new GetTownRes(
                        rs.getInt("townPostId"),
                        rs.getInt("userId"),
                        rs.getString("content"))
                );
    }



    public List<GetTownRes> getTown(int categoryId) {
        String getTownQuery = "select * from TownPost where townPostCategoryId = ?";
        int getTownParams = categoryId;
        return this.jdbcTemplate.query(
                getTownQuery,
                (rs, rowNum) -> new GetTownRes(
                        rs.getInt("townPostId"),
                        rs.getInt("userId"),
                        rs.getString("content")),
                getTownParams);
    }

    public int createTown(PostTownNewReq postTownNewReq) {
        String createTownQuery = "insert into TownPost (userId, townPostCategoryId, content, townPostLocationId) VALUES (?,?,?,?)";
        Object[] createTownParams = new Object[]{postTownNewReq.getUserId(), postTownNewReq.getTownPostCategoryId(), postTownNewReq.getContent(), postTownNewReq.getTownPostLocationId()};
        this.jdbcTemplate.update(createTownQuery, createTownParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }


}
