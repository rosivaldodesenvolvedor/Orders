package com.example.ordes.repositories;

import com.example.ordes.models.CartModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<CartModel, UUID> {

}
