package cherry.spring.tutorial.db.gen.mapper;

import cherry.spring.tutorial.db.gen.dto.Todo;
import cherry.spring.tutorial.db.gen.dto.TodoCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.JdbcType;

public interface TodoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    @SelectProvider(type=TodoSqlProvider.class, method="countByExample")
    int countByExample(TodoCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    @DeleteProvider(type=TodoSqlProvider.class, method="deleteByExample")
    int deleteByExample(TodoCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    @Delete({
        "delete from TODO",
        "where ID = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    @Insert({
        "insert into TODO (POSTED_BY, POSTED_AT, ",
        "DONE_AT, DONE_FLG, ",
        "DESCRIPTION, UPDATED_AT, ",
        "CREATED_AT, LOCK_VERSION, ",
        "DELETED_FLG)",
        "values (#{postedBy,jdbcType=VARCHAR}, #{postedAt,jdbcType=TIMESTAMP}, ",
        "#{doneAt,jdbcType=TIMESTAMP}, #{doneFlg,jdbcType=INTEGER}, ",
        "#{description,jdbcType=VARCHAR}, #{updatedAt,jdbcType=TIMESTAMP}, ",
        "#{createdAt,jdbcType=TIMESTAMP}, #{lockVersion,jdbcType=INTEGER}, ",
        "#{deletedFlg,jdbcType=INTEGER})"
    })
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insert(Todo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    @InsertProvider(type=TodoSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insertSelective(Todo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    @SelectProvider(type=TodoSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="ID", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="POSTED_BY", property="postedBy", jdbcType=JdbcType.VARCHAR),
        @Result(column="POSTED_AT", property="postedAt", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="DONE_AT", property="doneAt", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="DONE_FLG", property="doneFlg", jdbcType=JdbcType.INTEGER),
        @Result(column="DESCRIPTION", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="UPDATED_AT", property="updatedAt", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="CREATED_AT", property="createdAt", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="LOCK_VERSION", property="lockVersion", jdbcType=JdbcType.INTEGER),
        @Result(column="DELETED_FLG", property="deletedFlg", jdbcType=JdbcType.INTEGER)
    })
    List<Todo> selectByExampleWithRowbounds(TodoCriteria example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    @SelectProvider(type=TodoSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="ID", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="POSTED_BY", property="postedBy", jdbcType=JdbcType.VARCHAR),
        @Result(column="POSTED_AT", property="postedAt", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="DONE_AT", property="doneAt", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="DONE_FLG", property="doneFlg", jdbcType=JdbcType.INTEGER),
        @Result(column="DESCRIPTION", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="UPDATED_AT", property="updatedAt", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="CREATED_AT", property="createdAt", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="LOCK_VERSION", property="lockVersion", jdbcType=JdbcType.INTEGER),
        @Result(column="DELETED_FLG", property="deletedFlg", jdbcType=JdbcType.INTEGER)
    })
    List<Todo> selectByExample(TodoCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    @Select({
        "select",
        "ID, POSTED_BY, POSTED_AT, DONE_AT, DONE_FLG, DESCRIPTION, UPDATED_AT, CREATED_AT, ",
        "LOCK_VERSION, DELETED_FLG",
        "from TODO",
        "where ID = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="ID", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="POSTED_BY", property="postedBy", jdbcType=JdbcType.VARCHAR),
        @Result(column="POSTED_AT", property="postedAt", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="DONE_AT", property="doneAt", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="DONE_FLG", property="doneFlg", jdbcType=JdbcType.INTEGER),
        @Result(column="DESCRIPTION", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="UPDATED_AT", property="updatedAt", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="CREATED_AT", property="createdAt", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="LOCK_VERSION", property="lockVersion", jdbcType=JdbcType.INTEGER),
        @Result(column="DELETED_FLG", property="deletedFlg", jdbcType=JdbcType.INTEGER)
    })
    Todo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    @UpdateProvider(type=TodoSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") Todo record, @Param("example") TodoCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    @UpdateProvider(type=TodoSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") Todo record, @Param("example") TodoCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    @UpdateProvider(type=TodoSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Todo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    @Update({
        "update TODO",
        "set POSTED_BY = #{postedBy,jdbcType=VARCHAR},",
          "POSTED_AT = #{postedAt,jdbcType=TIMESTAMP},",
          "DONE_AT = #{doneAt,jdbcType=TIMESTAMP},",
          "DONE_FLG = #{doneFlg,jdbcType=INTEGER},",
          "DESCRIPTION = #{description,jdbcType=VARCHAR},",
          "UPDATED_AT = #{updatedAt,jdbcType=TIMESTAMP},",
          "CREATED_AT = #{createdAt,jdbcType=TIMESTAMP},",
          "LOCK_VERSION = #{lockVersion,jdbcType=INTEGER},",
          "DELETED_FLG = #{deletedFlg,jdbcType=INTEGER}",
        "where ID = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Todo record);
}