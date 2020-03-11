package com.ddanilyuk.userDemo1.repositories;

import com.ddanilyuk.userDemo1.model.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DeadlineRepository extends JpaRepository<Deadline, Integer> {

//    List<Deadline> findByProject(int id);

}