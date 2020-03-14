package com.ddanilyuk.userDemo1.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usr")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    private String userFirstName;

    private String userSecondName;

    private String username;

    private String password;

    @Column(name = "uuid", updatable = false, nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID uuid;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Project> projects = new ArrayList<>();

    public User() {
    }

    public User(String userFirstName, String userSecondName) {
        this.userFirstName = userFirstName;
        this.userSecondName = userSecondName;
    }

    public User(String userFirstName, String userSecondName, String username, String password) {
        this.userFirstName = userFirstName;
        this.userSecondName = userSecondName;
        this.username = username;
        this.password = password;
        this.uuid = UUID.randomUUID();
    }

    public User(String userFirstName, String userSecondName, String username, String password, List<Project> projects) {
        this.userFirstName = userFirstName;
        this.userSecondName = userSecondName;
        this.username = username;
        this.password = password;
        this.projects = projects;
    }

    public User(String userFirstName, String userSecondName, List<Project> projects) {
        this.userFirstName = userFirstName;
        this.userSecondName = userSecondName;
        this.projects = projects;
    }


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserSecondName() {
        return userSecondName;
    }

    public void setUserSecondName(String userSecondName) {
        this.userSecondName = userSecondName;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userFirstName='" + userFirstName + '\'' +
                ", userSecondName='" + userSecondName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", uuid=" + uuid +
                ", projects=" + projects +
                '}';
    }
}
