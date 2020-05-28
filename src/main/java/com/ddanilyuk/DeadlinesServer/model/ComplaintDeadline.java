package com.ddanilyuk.DeadlinesServer.model;

import java.util.List;

public class ComplaintDeadline {
    public Deadline deadline;
    // ліст стрінгів з юзернеймами які порібно додати
    public List<String> usersToAdd;

    public ComplaintDeadline(Deadline deadline, List<String> usersToAdd) {
        this.deadline = deadline;
        this.usersToAdd = usersToAdd;
    }
}
