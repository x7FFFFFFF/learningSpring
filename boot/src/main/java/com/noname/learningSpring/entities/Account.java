package com.noname.learningSpring.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "Accounts")
public class Account implements IEntity<Account>{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;



    @Column(name = "User_Name", length = 20, nullable = false)
    private String name;

    @Column(name = "Encryted_Password", length = 128, nullable = false)
    private String encrytedPassword;

    @Column(name = "Active", length = 1, nullable = false)
    private boolean active;


    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    protected Account() {
    }

    public Account(String userName, String encrytedPassword, boolean active,  Collection<Role> roles) {
        this.name = userName;
        this.encrytedPassword = encrytedPassword;
        this.active = active;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public EntityReference<Account> toReference() {
        return new EntityReference<>(id, name, Account.class);
    }

    public String getEncrytedPassword() {
        return encrytedPassword;
    }

    public void setEncrytedPassword(String encrytedPassword) {
        this.encrytedPassword = encrytedPassword;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", encrytedPassword='" + encrytedPassword + '\'' +
                ", active=" + active +
                ", roles=" + roles +
                '}';
    }
}
