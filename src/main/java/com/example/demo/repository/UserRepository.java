package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.User;
@Repository

public interface UserRepository extends JpaRepository<User,Long> {
	
	User findByUserName(String userName);
	@Query("select u from User u where u.email = ?1")

    User findByEmail(String email);
    
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.userName = ?1")

    boolean existsByUserName(String userName);
   // @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.email = ?1")
//    boolean existsByUsername(String username);
//
//    boolean existsByEmail(String email); 
    
	@Query("select u.userName from User u where u.userId = ?1")
    Optional<User> findByUserId(UUID userId);

}
