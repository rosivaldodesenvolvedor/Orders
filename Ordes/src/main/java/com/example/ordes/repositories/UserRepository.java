package com.example.ordes.repositories;

import com.example.ordes.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {
    UserDetails findByUserName(String userName);
}
