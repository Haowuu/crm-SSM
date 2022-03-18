package com.wh.crm.workbench.mapper;

import com.wh.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Thu Mar 10 23:22:38 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Thu Mar 10 23:22:38 CST 2022
     */
    int insert(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Thu Mar 10 23:22:38 CST 2022
     */
    int insertSelective(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Thu Mar 10 23:22:38 CST 2022
     */
    ClueActivityRelation selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Thu Mar 10 23:22:38 CST 2022
     */
    int updateByPrimaryKeySelective(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Thu Mar 10 23:22:38 CST 2022
     */
    int updateByPrimaryKey(ClueActivityRelation record);

    int saveActivityAndClueRelation(List<ClueActivityRelation> clueActivityRelationList);

    int deleteClueAndActivityBind(ClueActivityRelation clueActivityRelation);

    List<ClueActivityRelation> queryClueActivityRelationMapperByClueId(String clueId);

    int deleteClueActivityRelationByClueId(String clueId);
}