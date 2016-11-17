package diamond.cms.server.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "c_catalog")
@JsonInclude(Include.NON_EMPTY)
public class Catalog {
    @Id
    @GenericGenerator(strategy = "uuid", name = "system-uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Catalog parent;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Catalog getParent() {
        return parent;
    }

    public void setParent(Catalog parent) {
        this.parent = parent;
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
