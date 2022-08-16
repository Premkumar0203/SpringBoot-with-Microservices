package com.digitalbooks.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.digitalbooks.entities.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> 
{

	@Query("select count(u) from Users u where u.userName = ?1 and u.password =?2")
	int findByName(String userName, String password);

	@Query("select u from Users u where u.userName = ?1 and u.password= ?2 ")
	Optional<Users> getUserDetails(String username, String password);

	@Query("select u from Users u where u.userName = ?1 ")
	Optional<Users> fetchUserName(String username);


}
