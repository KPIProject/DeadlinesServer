package com.ddanilyuk.DeadlinesServer.repositories;

import com.ddanilyuk.DeadlinesServer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserId(int id);

    Optional<User> findByUsername(String username);

//    User findByUuid(UUID uuid);

    Optional<User> findUserByUuid(UUID uuid);

//    Optional<User> findByUuidEquals(UUID uuid);

    @Query("select us from User us where us.uuid = ?1")
    Optional<User> findUserByUuidEqual(UUID uuidGiven);

// запрос який здійснює пошук серед юзерів зі схожим юзернеймом який ви ввели
    @Query("select u from User u where u.username like %?1%")
    Optional<List<User>> findAllByUsername(String username);

}