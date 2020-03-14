package com.ddanilyuk.userDemo1.repositories;

import com.ddanilyuk.userDemo1.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Optional<Project> findByProjectId(int id);

}