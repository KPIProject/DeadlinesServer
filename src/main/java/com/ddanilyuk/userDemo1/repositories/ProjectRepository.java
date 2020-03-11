package com.ddanilyuk.userDemo1.repositories;

import com.ddanilyuk.userDemo1.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Project findByProjectId(int id);

}