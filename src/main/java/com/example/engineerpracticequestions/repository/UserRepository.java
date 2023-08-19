package com.example.engineerpracticequestions.repository;

import com.example.engineerpracticequestions.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    User findByEmailAndPassword(String email,String password);
    User findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.coverImage = :coverImage WHERE u.userId = :userId")
    void updateUserCoverImage(Long userId, byte[] coverImage);
}
