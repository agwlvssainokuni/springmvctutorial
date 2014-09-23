package cherry.spring.tutorial.db.gen.dto;

import cherry.spring.common.custom.DeletedFlag;
import cherry.spring.common.custom.FlagCode;
import java.io.Serializable;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class Todo implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TODO.ID
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TODO.POSTED_BY
     *
     * @mbggenerated
     */
    private String postedBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TODO.POSTED_AT
     *
     * @mbggenerated
     */
    private LocalDateTime postedAt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TODO.DUE_DATE
     *
     * @mbggenerated
     */
    private LocalDate dueDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TODO.DONE_AT
     *
     * @mbggenerated
     */
    private LocalDateTime doneAt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TODO.DONE_FLG
     *
     * @mbggenerated
     */
    private FlagCode doneFlg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TODO.DESCRIPTION
     *
     * @mbggenerated
     */
    private String description;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TODO.UPDATED_AT
     *
     * @mbggenerated
     */
    private LocalDateTime updatedAt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TODO.CREATED_AT
     *
     * @mbggenerated
     */
    private LocalDateTime createdAt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TODO.LOCK_VERSION
     *
     * @mbggenerated
     */
    private Integer lockVersion;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TODO.DELETED_FLG
     *
     * @mbggenerated
     */
    private DeletedFlag deletedFlg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table TODO
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TODO.ID
     *
     * @return the value of TODO.ID
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TODO.ID
     *
     * @param id the value for TODO.ID
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TODO.POSTED_BY
     *
     * @return the value of TODO.POSTED_BY
     *
     * @mbggenerated
     */
    public String getPostedBy() {
        return postedBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TODO.POSTED_BY
     *
     * @param postedBy the value for TODO.POSTED_BY
     *
     * @mbggenerated
     */
    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TODO.POSTED_AT
     *
     * @return the value of TODO.POSTED_AT
     *
     * @mbggenerated
     */
    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TODO.POSTED_AT
     *
     * @param postedAt the value for TODO.POSTED_AT
     *
     * @mbggenerated
     */
    public void setPostedAt(LocalDateTime postedAt) {
        this.postedAt = postedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TODO.DUE_DATE
     *
     * @return the value of TODO.DUE_DATE
     *
     * @mbggenerated
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TODO.DUE_DATE
     *
     * @param dueDate the value for TODO.DUE_DATE
     *
     * @mbggenerated
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TODO.DONE_AT
     *
     * @return the value of TODO.DONE_AT
     *
     * @mbggenerated
     */
    public LocalDateTime getDoneAt() {
        return doneAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TODO.DONE_AT
     *
     * @param doneAt the value for TODO.DONE_AT
     *
     * @mbggenerated
     */
    public void setDoneAt(LocalDateTime doneAt) {
        this.doneAt = doneAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TODO.DONE_FLG
     *
     * @return the value of TODO.DONE_FLG
     *
     * @mbggenerated
     */
    public FlagCode getDoneFlg() {
        return doneFlg;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TODO.DONE_FLG
     *
     * @param doneFlg the value for TODO.DONE_FLG
     *
     * @mbggenerated
     */
    public void setDoneFlg(FlagCode doneFlg) {
        this.doneFlg = doneFlg;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TODO.DESCRIPTION
     *
     * @return the value of TODO.DESCRIPTION
     *
     * @mbggenerated
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TODO.DESCRIPTION
     *
     * @param description the value for TODO.DESCRIPTION
     *
     * @mbggenerated
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TODO.UPDATED_AT
     *
     * @return the value of TODO.UPDATED_AT
     *
     * @mbggenerated
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TODO.UPDATED_AT
     *
     * @param updatedAt the value for TODO.UPDATED_AT
     *
     * @mbggenerated
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TODO.CREATED_AT
     *
     * @return the value of TODO.CREATED_AT
     *
     * @mbggenerated
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TODO.CREATED_AT
     *
     * @param createdAt the value for TODO.CREATED_AT
     *
     * @mbggenerated
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TODO.LOCK_VERSION
     *
     * @return the value of TODO.LOCK_VERSION
     *
     * @mbggenerated
     */
    public Integer getLockVersion() {
        return lockVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TODO.LOCK_VERSION
     *
     * @param lockVersion the value for TODO.LOCK_VERSION
     *
     * @mbggenerated
     */
    public void setLockVersion(Integer lockVersion) {
        this.lockVersion = lockVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TODO.DELETED_FLG
     *
     * @return the value of TODO.DELETED_FLG
     *
     * @mbggenerated
     */
    public DeletedFlag getDeletedFlg() {
        return deletedFlg;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TODO.DELETED_FLG
     *
     * @param deletedFlg the value for TODO.DELETED_FLG
     *
     * @mbggenerated
     */
    public void setDeletedFlg(DeletedFlag deletedFlg) {
        this.deletedFlg = deletedFlg;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Todo other = (Todo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPostedBy() == null ? other.getPostedBy() == null : this.getPostedBy().equals(other.getPostedBy()))
            && (this.getPostedAt() == null ? other.getPostedAt() == null : this.getPostedAt().equals(other.getPostedAt()))
            && (this.getDueDate() == null ? other.getDueDate() == null : this.getDueDate().equals(other.getDueDate()))
            && (this.getDoneAt() == null ? other.getDoneAt() == null : this.getDoneAt().equals(other.getDoneAt()))
            && (this.getDoneFlg() == null ? other.getDoneFlg() == null : this.getDoneFlg().equals(other.getDoneFlg()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getLockVersion() == null ? other.getLockVersion() == null : this.getLockVersion().equals(other.getLockVersion()))
            && (this.getDeletedFlg() == null ? other.getDeletedFlg() == null : this.getDeletedFlg().equals(other.getDeletedFlg()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPostedBy() == null) ? 0 : getPostedBy().hashCode());
        result = prime * result + ((getPostedAt() == null) ? 0 : getPostedAt().hashCode());
        result = prime * result + ((getDueDate() == null) ? 0 : getDueDate().hashCode());
        result = prime * result + ((getDoneAt() == null) ? 0 : getDoneAt().hashCode());
        result = prime * result + ((getDoneFlg() == null) ? 0 : getDoneFlg().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getLockVersion() == null) ? 0 : getLockVersion().hashCode());
        result = prime * result + ((getDeletedFlg() == null) ? 0 : getDeletedFlg().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TODO
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", postedBy=").append(postedBy);
        sb.append(", postedAt=").append(postedAt);
        sb.append(", dueDate=").append(dueDate);
        sb.append(", doneAt=").append(doneAt);
        sb.append(", doneFlg=").append(doneFlg);
        sb.append(", description=").append(description);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", lockVersion=").append(lockVersion);
        sb.append(", deletedFlg=").append(deletedFlg);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}