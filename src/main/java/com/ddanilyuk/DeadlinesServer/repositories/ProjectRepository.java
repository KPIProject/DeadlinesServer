package com.ddanilyuk.DeadlinesServer.repositories;

import com.ddanilyuk.DeadlinesServer.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Optional<Project> findByProjectId(int id);

}