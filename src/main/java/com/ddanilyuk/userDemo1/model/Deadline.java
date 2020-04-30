package com.ddanilyuk.userDemo1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;


@SuppressWarnings("unused")
@Entity
@Table(name = "deadline")
@JsonIgnoreProperties("project")
public class Deadline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.deadlinesView.class)
    private int deadlineId;


    @JsonView(Views.deadlinesView.class)
    private String deadlineName;

    @Size(max = 8192)
    @JsonView(Views.deadlinesView.class)
    private String deadlineDescription;


    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;


    @NotNull
    @JsonView(Views.deadlinesView.class)
    private int deadlineProjectId;


    @Column
//    @Transient
    @ElementCollection(targetClass = UUID.class)
//    @JsonView(Views.deadlinesView.class)
    private List<UUID> deadlineExecutorsUuid = new ArrayList<>();


    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    @Transient
    @JsonView(Views.deadlinesView.class)
    private List<User> deadlineExecutors = new ArrayList<>();


    @Column
    @JsonView(Views.defaultView.class)
    private long deadlineCreatedTime;


    @Column
    @JsonView(Views.defaultView.class)
    private long deadlineExecutionTime;


    public Deadline() {

    }

    public Deadline(String deadlineName, String deadlineDescription) {
        this.deadlineName = deadlineName;
        this.deadlineDescription = deadlineDescription;

        Date dateNow = new Date();
        deadlineCreatedTime = dateNow.getTime();
    }

    public Deadline(String deadlineName, String deadlineDescription, Project project) {
        this.deadlineName = deadlineName;
        this.deadlineDescription = deadlineDescription;
        this.project = project;
        this.deadlineProjectId = project.getProjectId();

        Date dateNow = new Date();
        deadlineCreatedTime = dateNow.getTime();
    }

    public long getDeadlineCreatedTime() {
        return deadlineCreatedTime;
    }

    public void setDeadlineCreatedTime(long deadlineCreatedTime) {
        this.deadlineCreatedTime = deadlineCreatedTime;
    }

    public long getDeadlineExecutionTime() {
        return deadlineExecutionTime;
    }

    public void setDeadlineExecutionTime(long deadlineExecutionTime) {
        this.deadlineExecutionTime = deadlineExecutionTime;
    }

    public List<UUID> getDeadlineExecutorsUuid() {
        return deadlineExecutorsUuid;
    }

    public void setDeadlineExecutorsUuid(List<UUID> deadlineExecutorsUuid) {
        this.deadlineExecutorsUuid = deadlineExecutorsUuid;
    }

    public List<User> getDeadlineExecutors() {
        List<User> usersAll = project.getProjectUsers();
        List<User> deadlineExecutors = new ArrayList<>();

        for (User user : usersAll) {
            if (deadlineExecutorsUuid.contains(user.getUuid())) {
                deadlineExecutors.add(user);
            }
        }

        return deadlineExecutors;
    }

    public void setDeadlineExecutors(List<User> deadlineExecutors) {
        this.deadlineExecutors = deadlineExecutors;
    }

    public int getDeadlineProjectId() {
        return deadlineProjectId;
    }

    public void setDeadlineProjectId(int deadlineProjectId) {
        this.deadlineProjectId = deadlineProjectId;
    }

    public int getDeadlineId() {
        return deadlineId;
    }

    public void setDeadlineId(int deadlineId) {
        this.deadlineId = deadlineId;
    }

    public String getDeadlineName() {
        return deadlineName;
    }

    public void setDeadlineName(String deadlineName) {
        this.deadlineName = deadlineName;
    }

    public String getDeadlineDescription() {
        return deadlineDescription;
    }

    public void setDeadlineDescription(String deadlineDescription) {
        this.deadlineDescription = deadlineDescription;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
