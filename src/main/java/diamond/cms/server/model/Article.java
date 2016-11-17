package diamond.cms.server.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name="c_article")
@JsonInclude(Include.NON_EMPTY)
public class Article {
    @Id
    @GenericGenerator(strategy="uuid", name="system-uuid")
    @GeneratedValue(generator="system-uuid")
    private String id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, columnDefinition = "text")
    private String content;
    @Column(nullable = false, columnDefinition = "datetime default now()")
    private Date createTime;
    @Column(nullable = false, columnDefinition = "datetime default now()")
    private Date updateTime;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


}
