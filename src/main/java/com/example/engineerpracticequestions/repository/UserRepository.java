package com.example.engineerpracticequestions.repository;

import com.example.engineerpracticequestions.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
