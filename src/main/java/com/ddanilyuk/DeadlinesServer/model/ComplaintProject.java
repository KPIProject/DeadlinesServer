package com.ddanilyuk.DeadlinesServer.model;

import java.util.List;
// коли створюєш проект додаєш проект і список всіх юзерів
public class ComplaintProject {
    public Project project;
    public List<String> usersToAdd;

    public ComplaintProject(Project project, List<String> usersToAdd) {
        this.project = project;
        this.usersToAdd = usersToAdd;
    }
}
