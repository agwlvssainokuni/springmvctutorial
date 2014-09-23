package cherry.spring.tutorial.db.gen.query;

import javax.annotation.Generated;

/**
 * BTodo is a Querydsl bean type
 */
@Generated("com.mysema.query.codegen.BeanSerializer")
public class BTodo {

    private org.joda.time.LocalDateTime createdAt;

    private Integer deletedFlg;

    private String description;

    private org.joda.time.LocalDateTime doneAt;

    private Integer doneFlg;

    private Integer id;

    private Integer lockVersion;

    private org.joda.time.LocalDateTime postedAt;

    private String postedBy;

    private org.joda.time.LocalDateTime updatedAt;

    public org.joda.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(org.joda.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getDeletedFlg() {
        return deletedFlg;
    }

    public void setDeletedFlg(Integer deletedFlg) {
        this.deletedFlg = deletedFlg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public org.joda.time.LocalDateTime getDoneAt() {
        return doneAt;
    }

    public void setDoneAt(org.joda.time.LocalDateTime doneAt) {
        this.doneAt = doneAt;
    }

    public Integer getDoneFlg() {
        return doneFlg;
    }

    public void setDoneFlg(Integer doneFlg) {
        this.doneFlg = doneFlg;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLockVersion() {
        return lockVersion;
    }

    public void setLockVersion(Integer lockVersion) {
        this.lockVersion = lockVersion;
    }

    public org.joda.time.LocalDateTime getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(org.joda.time.LocalDateTime postedAt) {
        this.postedAt = postedAt;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public org.joda.time.LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(org.joda.time.LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String toString() {
         return "createdAt = " + createdAt + ", deletedFlg = " + deletedFlg + ", description = " + description + ", doneAt = " + doneAt + ", doneFlg = " + doneFlg + ", id = " + id + ", lockVersion = " + lockVersion + ", postedAt = " + postedAt + ", postedBy = " + postedBy + ", updatedAt = " + updatedAt;
    }

}

