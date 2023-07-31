package com.example.ordes.controllers;

import com.example.ordes.dtos.UserDTO;
import com.example.ordes.models.Users;
import com.example.ordes.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity register(@RequestBody @Valid UserDTO data){
        if(this.userRepository.findByUserName(data.userName()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        Users newUser = new Users(data.userName(), encryptedPassword, data.role());

        this.userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body( this.userRepository.save(newUser));
    }

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers(){
        List<Users> userList = userRepository.findAll();
        if(!userList.isEmpty()) {
            for(Users user : userList) {
                UUID id = user.getId();
                user.add(linkTo(methodOn(UserController.class).getOneUser(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value="id") UUID id){
        Optional<Users> user = userRepository.findById(id);
        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        user.get().add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("Products List"));
        return ResponseEntity.status(HttpStatus.OK).body(user.get());
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value="id") UUID id) {
        Optional<Users> user = userRepository.findById(id);
        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        userRepository.delete(user.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(value="id") UUID id, @RequestBody UserDTO userRecordDto) {
        Optional<Users> user = userRepository.findById(id);
        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        var userModel = user.get();
        BeanUtils.copyProperties(userRecordDto, userModel);
        return ResponseEntity.status(HttpStatus.OK).body(this.userRepository.save(userModel));
    }

}
