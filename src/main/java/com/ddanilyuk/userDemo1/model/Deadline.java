package com.ddanilyuk.userDemo1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


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


    @JsonView(Views.deadlinesView.class)
    private String deadlineDescription;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;


    @NotNull
    @JsonView(Views.deadlinesView.class)
    private int deadlineProjectId;


    @Column
    @ElementCollection(targetClass=UUID.class)
    @JsonView(Views.deadlinesView.class)
    private List<UUID> deadlineExecutorsUuid = new ArrayList<>();


    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    @Transient
    @JsonView(Views.deadlinesDetailView.class)
    private List<User> deadlineExecutors = new ArrayList<>();

    public Deadline() {

    }

    public Deadline(String deadlineName, String deadlineDescription) {
        this.deadlineName = deadlineName;
        this.deadlineDescription = deadlineDescription;
    }

    public Deadline(String deadlineName, String deadlineDescription, Project project) {
        this.deadlineName = deadlineName;
        this.deadlineDescription = deadlineDescription;
        this.project = project;
        this.deadlineProjectId = project.getProjectId();
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

        for (User user: usersAll) {
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
