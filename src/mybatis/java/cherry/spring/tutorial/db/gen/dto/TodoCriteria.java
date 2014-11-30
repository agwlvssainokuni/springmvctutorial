package cherry.spring.tutorial.db.gen.dto;

import cherry.foundation.type.DeletedFlag;
import cherry.foundation.type.FlagCode;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class TodoCriteria {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table TODO
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table TODO
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table TODO
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    public TodoCriteria() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table TODO
     *
     * @mbggenerated
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("ID is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("ID is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andPostedByIsNull() {
            addCriterion("POSTED_BY is null");
            return (Criteria) this;
        }

        public Criteria andPostedByIsNotNull() {
            addCriterion("POSTED_BY is not null");
            return (Criteria) this;
        }

        public Criteria andPostedByEqualTo(String value) {
            addCriterion("POSTED_BY =", value, "postedBy");
            return (Criteria) this;
        }

        public Criteria andPostedByNotEqualTo(String value) {
            addCriterion("POSTED_BY <>", value, "postedBy");
            return (Criteria) this;
        }

        public Criteria andPostedByGreaterThan(String value) {
            addCriterion("POSTED_BY >", value, "postedBy");
            return (Criteria) this;
        }

        public Criteria andPostedByGreaterThanOrEqualTo(String value) {
            addCriterion("POSTED_BY >=", value, "postedBy");
            return (Criteria) this;
        }

        public Criteria andPostedByLessThan(String value) {
            addCriterion("POSTED_BY <", value, "postedBy");
            return (Criteria) this;
        }

        public Criteria andPostedByLessThanOrEqualTo(String value) {
            addCriterion("POSTED_BY <=", value, "postedBy");
            return (Criteria) this;
        }

        public Criteria andPostedByLike(String value) {
            addCriterion("POSTED_BY like", value, "postedBy");
            return (Criteria) this;
        }

        public Criteria andPostedByNotLike(String value) {
            addCriterion("POSTED_BY not like", value, "postedBy");
            return (Criteria) this;
        }

        public Criteria andPostedByIn(List<String> values) {
            addCriterion("POSTED_BY in", values, "postedBy");
            return (Criteria) this;
        }

        public Criteria andPostedByNotIn(List<String> values) {
            addCriterion("POSTED_BY not in", values, "postedBy");
            return (Criteria) this;
        }

        public Criteria andPostedByBetween(String value1, String value2) {
            addCriterion("POSTED_BY between", value1, value2, "postedBy");
            return (Criteria) this;
        }

        public Criteria andPostedByNotBetween(String value1, String value2) {
            addCriterion("POSTED_BY not between", value1, value2, "postedBy");
            return (Criteria) this;
        }

        public Criteria andPostedAtIsNull() {
            addCriterion("POSTED_AT is null");
            return (Criteria) this;
        }

        public Criteria andPostedAtIsNotNull() {
            addCriterion("POSTED_AT is not null");
            return (Criteria) this;
        }

        public Criteria andPostedAtEqualTo(LocalDateTime value) {
            addCriterion("POSTED_AT =", value, "postedAt");
            return (Criteria) this;
        }

        public Criteria andPostedAtNotEqualTo(LocalDateTime value) {
            addCriterion("POSTED_AT <>", value, "postedAt");
            return (Criteria) this;
        }

        public Criteria andPostedAtGreaterThan(LocalDateTime value) {
            addCriterion("POSTED_AT >", value, "postedAt");
            return (Criteria) this;
        }

        public Criteria andPostedAtGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("POSTED_AT >=", value, "postedAt");
            return (Criteria) this;
        }

        public Criteria andPostedAtLessThan(LocalDateTime value) {
            addCriterion("POSTED_AT <", value, "postedAt");
            return (Criteria) this;
        }

        public Criteria andPostedAtLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("POSTED_AT <=", value, "postedAt");
            return (Criteria) this;
        }

        public Criteria andPostedAtIn(List<LocalDateTime> values) {
            addCriterion("POSTED_AT in", values, "postedAt");
            return (Criteria) this;
        }

        public Criteria andPostedAtNotIn(List<LocalDateTime> values) {
            addCriterion("POSTED_AT not in", values, "postedAt");
            return (Criteria) this;
        }

        public Criteria andPostedAtBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("POSTED_AT between", value1, value2, "postedAt");
            return (Criteria) this;
        }

        public Criteria andPostedAtNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("POSTED_AT not between", value1, value2, "postedAt");
            return (Criteria) this;
        }

        public Criteria andDueDateIsNull() {
            addCriterion("DUE_DATE is null");
            return (Criteria) this;
        }

        public Criteria andDueDateIsNotNull() {
            addCriterion("DUE_DATE is not null");
            return (Criteria) this;
        }

        public Criteria andDueDateEqualTo(LocalDate value) {
            addCriterion("DUE_DATE =", value, "dueDate");
            return (Criteria) this;
        }

        public Criteria andDueDateNotEqualTo(LocalDate value) {
            addCriterion("DUE_DATE <>", value, "dueDate");
            return (Criteria) this;
        }

        public Criteria andDueDateGreaterThan(LocalDate value) {
            addCriterion("DUE_DATE >", value, "dueDate");
            return (Criteria) this;
        }

        public Criteria andDueDateGreaterThanOrEqualTo(LocalDate value) {
            addCriterion("DUE_DATE >=", value, "dueDate");
            return (Criteria) this;
        }

        public Criteria andDueDateLessThan(LocalDate value) {
            addCriterion("DUE_DATE <", value, "dueDate");
            return (Criteria) this;
        }

        public Criteria andDueDateLessThanOrEqualTo(LocalDate value) {
            addCriterion("DUE_DATE <=", value, "dueDate");
            return (Criteria) this;
        }

        public Criteria andDueDateIn(List<LocalDate> values) {
            addCriterion("DUE_DATE in", values, "dueDate");
            return (Criteria) this;
        }

        public Criteria andDueDateNotIn(List<LocalDate> values) {
            addCriterion("DUE_DATE not in", values, "dueDate");
            return (Criteria) this;
        }

        public Criteria andDueDateBetween(LocalDate value1, LocalDate value2) {
            addCriterion("DUE_DATE between", value1, value2, "dueDate");
            return (Criteria) this;
        }

        public Criteria andDueDateNotBetween(LocalDate value1, LocalDate value2) {
            addCriterion("DUE_DATE not between", value1, value2, "dueDate");
            return (Criteria) this;
        }

        public Criteria andDoneAtIsNull() {
            addCriterion("DONE_AT is null");
            return (Criteria) this;
        }

        public Criteria andDoneAtIsNotNull() {
            addCriterion("DONE_AT is not null");
            return (Criteria) this;
        }

        public Criteria andDoneAtEqualTo(LocalDateTime value) {
            addCriterion("DONE_AT =", value, "doneAt");
            return (Criteria) this;
        }

        public Criteria andDoneAtNotEqualTo(LocalDateTime value) {
            addCriterion("DONE_AT <>", value, "doneAt");
            return (Criteria) this;
        }

        public Criteria andDoneAtGreaterThan(LocalDateTime value) {
            addCriterion("DONE_AT >", value, "doneAt");
            return (Criteria) this;
        }

        public Criteria andDoneAtGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("DONE_AT >=", value, "doneAt");
            return (Criteria) this;
        }

        public Criteria andDoneAtLessThan(LocalDateTime value) {
            addCriterion("DONE_AT <", value, "doneAt");
            return (Criteria) this;
        }

        public Criteria andDoneAtLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("DONE_AT <=", value, "doneAt");
            return (Criteria) this;
        }

        public Criteria andDoneAtIn(List<LocalDateTime> values) {
            addCriterion("DONE_AT in", values, "doneAt");
            return (Criteria) this;
        }

        public Criteria andDoneAtNotIn(List<LocalDateTime> values) {
            addCriterion("DONE_AT not in", values, "doneAt");
            return (Criteria) this;
        }

        public Criteria andDoneAtBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("DONE_AT between", value1, value2, "doneAt");
            return (Criteria) this;
        }

        public Criteria andDoneAtNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("DONE_AT not between", value1, value2, "doneAt");
            return (Criteria) this;
        }

        public Criteria andDoneFlgIsNull() {
            addCriterion("DONE_FLG is null");
            return (Criteria) this;
        }

        public Criteria andDoneFlgIsNotNull() {
            addCriterion("DONE_FLG is not null");
            return (Criteria) this;
        }

        public Criteria andDoneFlgEqualTo(FlagCode value) {
            addCriterion("DONE_FLG =", value, "doneFlg");
            return (Criteria) this;
        }

        public Criteria andDoneFlgNotEqualTo(FlagCode value) {
            addCriterion("DONE_FLG <>", value, "doneFlg");
            return (Criteria) this;
        }

        public Criteria andDoneFlgGreaterThan(FlagCode value) {
            addCriterion("DONE_FLG >", value, "doneFlg");
            return (Criteria) this;
        }

        public Criteria andDoneFlgGreaterThanOrEqualTo(FlagCode value) {
            addCriterion("DONE_FLG >=", value, "doneFlg");
            return (Criteria) this;
        }

        public Criteria andDoneFlgLessThan(FlagCode value) {
            addCriterion("DONE_FLG <", value, "doneFlg");
            return (Criteria) this;
        }

        public Criteria andDoneFlgLessThanOrEqualTo(FlagCode value) {
            addCriterion("DONE_FLG <=", value, "doneFlg");
            return (Criteria) this;
        }

        public Criteria andDoneFlgIn(List<FlagCode> values) {
            addCriterion("DONE_FLG in", values, "doneFlg");
            return (Criteria) this;
        }

        public Criteria andDoneFlgNotIn(List<FlagCode> values) {
            addCriterion("DONE_FLG not in", values, "doneFlg");
            return (Criteria) this;
        }

        public Criteria andDoneFlgBetween(FlagCode value1, FlagCode value2) {
            addCriterion("DONE_FLG between", value1, value2, "doneFlg");
            return (Criteria) this;
        }

        public Criteria andDoneFlgNotBetween(FlagCode value1, FlagCode value2) {
            addCriterion("DONE_FLG not between", value1, value2, "doneFlg");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNull() {
            addCriterion("DESCRIPTION is null");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("DESCRIPTION is not null");
            return (Criteria) this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("DESCRIPTION =", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("DESCRIPTION <>", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("DESCRIPTION >", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("DESCRIPTION >=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("DESCRIPTION <", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("DESCRIPTION <=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("DESCRIPTION like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("DESCRIPTION not like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("DESCRIPTION in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("DESCRIPTION not in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("DESCRIPTION between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("DESCRIPTION not between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIsNull() {
            addCriterion("UPDATED_AT is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIsNotNull() {
            addCriterion("UPDATED_AT is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtEqualTo(LocalDateTime value) {
            addCriterion("UPDATED_AT =", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotEqualTo(LocalDateTime value) {
            addCriterion("UPDATED_AT <>", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtGreaterThan(LocalDateTime value) {
            addCriterion("UPDATED_AT >", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("UPDATED_AT >=", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtLessThan(LocalDateTime value) {
            addCriterion("UPDATED_AT <", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("UPDATED_AT <=", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIn(List<LocalDateTime> values) {
            addCriterion("UPDATED_AT in", values, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotIn(List<LocalDateTime> values) {
            addCriterion("UPDATED_AT not in", values, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("UPDATED_AT between", value1, value2, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("UPDATED_AT not between", value1, value2, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIsNull() {
            addCriterion("CREATED_AT is null");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIsNotNull() {
            addCriterion("CREATED_AT is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedAtEqualTo(LocalDateTime value) {
            addCriterion("CREATED_AT =", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotEqualTo(LocalDateTime value) {
            addCriterion("CREATED_AT <>", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtGreaterThan(LocalDateTime value) {
            addCriterion("CREATED_AT >", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("CREATED_AT >=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtLessThan(LocalDateTime value) {
            addCriterion("CREATED_AT <", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("CREATED_AT <=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIn(List<LocalDateTime> values) {
            addCriterion("CREATED_AT in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotIn(List<LocalDateTime> values) {
            addCriterion("CREATED_AT not in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("CREATED_AT between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("CREATED_AT not between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andLockVersionIsNull() {
            addCriterion("LOCK_VERSION is null");
            return (Criteria) this;
        }

        public Criteria andLockVersionIsNotNull() {
            addCriterion("LOCK_VERSION is not null");
            return (Criteria) this;
        }

        public Criteria andLockVersionEqualTo(Integer value) {
            addCriterion("LOCK_VERSION =", value, "lockVersion");
            return (Criteria) this;
        }

        public Criteria andLockVersionNotEqualTo(Integer value) {
            addCriterion("LOCK_VERSION <>", value, "lockVersion");
            return (Criteria) this;
        }

        public Criteria andLockVersionGreaterThan(Integer value) {
            addCriterion("LOCK_VERSION >", value, "lockVersion");
            return (Criteria) this;
        }

        public Criteria andLockVersionGreaterThanOrEqualTo(Integer value) {
            addCriterion("LOCK_VERSION >=", value, "lockVersion");
            return (Criteria) this;
        }

        public Criteria andLockVersionLessThan(Integer value) {
            addCriterion("LOCK_VERSION <", value, "lockVersion");
            return (Criteria) this;
        }

        public Criteria andLockVersionLessThanOrEqualTo(Integer value) {
            addCriterion("LOCK_VERSION <=", value, "lockVersion");
            return (Criteria) this;
        }

        public Criteria andLockVersionIn(List<Integer> values) {
            addCriterion("LOCK_VERSION in", values, "lockVersion");
            return (Criteria) this;
        }

        public Criteria andLockVersionNotIn(List<Integer> values) {
            addCriterion("LOCK_VERSION not in", values, "lockVersion");
            return (Criteria) this;
        }

        public Criteria andLockVersionBetween(Integer value1, Integer value2) {
            addCriterion("LOCK_VERSION between", value1, value2, "lockVersion");
            return (Criteria) this;
        }

        public Criteria andLockVersionNotBetween(Integer value1, Integer value2) {
            addCriterion("LOCK_VERSION not between", value1, value2, "lockVersion");
            return (Criteria) this;
        }

        public Criteria andDeletedFlgIsNull() {
            addCriterion("DELETED_FLG is null");
            return (Criteria) this;
        }

        public Criteria andDeletedFlgIsNotNull() {
            addCriterion("DELETED_FLG is not null");
            return (Criteria) this;
        }

        public Criteria andDeletedFlgEqualTo(DeletedFlag value) {
            addCriterion("DELETED_FLG =", value, "deletedFlg");
            return (Criteria) this;
        }

        public Criteria andDeletedFlgNotEqualTo(DeletedFlag value) {
            addCriterion("DELETED_FLG <>", value, "deletedFlg");
            return (Criteria) this;
        }

        public Criteria andDeletedFlgGreaterThan(DeletedFlag value) {
            addCriterion("DELETED_FLG >", value, "deletedFlg");
            return (Criteria) this;
        }

        public Criteria andDeletedFlgGreaterThanOrEqualTo(DeletedFlag value) {
            addCriterion("DELETED_FLG >=", value, "deletedFlg");
            return (Criteria) this;
        }

        public Criteria andDeletedFlgLessThan(DeletedFlag value) {
            addCriterion("DELETED_FLG <", value, "deletedFlg");
            return (Criteria) this;
        }

        public Criteria andDeletedFlgLessThanOrEqualTo(DeletedFlag value) {
            addCriterion("DELETED_FLG <=", value, "deletedFlg");
            return (Criteria) this;
        }

        public Criteria andDeletedFlgIn(List<DeletedFlag> values) {
            addCriterion("DELETED_FLG in", values, "deletedFlg");
            return (Criteria) this;
        }

        public Criteria andDeletedFlgNotIn(List<DeletedFlag> values) {
            addCriterion("DELETED_FLG not in", values, "deletedFlg");
            return (Criteria) this;
        }

        public Criteria andDeletedFlgBetween(DeletedFlag value1, DeletedFlag value2) {
            addCriterion("DELETED_FLG between", value1, value2, "deletedFlg");
            return (Criteria) this;
        }

        public Criteria andDeletedFlgNotBetween(DeletedFlag value1, DeletedFlag value2) {
            addCriterion("DELETED_FLG not between", value1, value2, "deletedFlg");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table TODO
     *
     * @mbggenerated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table TODO
     *
     * @mbggenerated
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}