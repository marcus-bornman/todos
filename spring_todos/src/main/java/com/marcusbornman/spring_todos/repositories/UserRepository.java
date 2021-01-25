package com.marcusbornman.spring_todos.repositories;

import com.marcusbornman.spring_todos.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
