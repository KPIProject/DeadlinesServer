package com.ddanilyuk.DeadlinesServer.repositories;

import com.ddanilyuk.DeadlinesServer.model.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeadlineRepository extends JpaRepository<Deadline, Integer> {

//    List<Deadline> findByProject(int id);

}