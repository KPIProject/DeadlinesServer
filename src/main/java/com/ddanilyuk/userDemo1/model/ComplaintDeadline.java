package com.ddanilyuk.userDemo1.model;

import java.util.List;

public class ComplaintDeadline {
    public Deadline deadline;
    public List<String> usersToAdd;

    public ComplaintDeadline(Deadline deadline, List<String> usersToAdd) {
        this.deadline = deadline;
        this.usersToAdd = usersToAdd;
    }
}
