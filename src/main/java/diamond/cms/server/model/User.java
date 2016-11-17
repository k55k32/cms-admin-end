package diamond.cms.server.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name="c_user")
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties({"id","password","token","expired"})
public class User {

    @Id
    @GenericGenerator(strategy="uuid", name="system-uuid")
    @GeneratedValue(generator="system-uuid")
    private String id;
    @NotBlank
    @Column(nullable = false, unique = true)
    private String username;
    @NotBlank
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private Date createTime;
    @Column(nullable = false)
    private Date lastLoginTime;

    private Long expired;

    private String token;


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getLastLoginTime() {
        return lastLoginTime;
    }
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
    public Long getExpired() {
        return expired;
    }
    public void setExpired(Long expired) {
        this.expired = expired;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

}
