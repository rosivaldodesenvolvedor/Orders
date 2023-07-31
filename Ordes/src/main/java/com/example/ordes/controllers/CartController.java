package com.example.ordes.controllers;


import com.example.ordes.dtos.CartRecordDTO;
import com.example.ordes.models.CartModel;
import com.example.ordes.repositories.CartRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
   private CartRepository cartRepository;


    @PostMapping
    public ResponseEntity<CartModel> saveItem(@RequestBody @Valid CartRecordDTO cartRecordDto) {
        var cartModel = new CartModel();
        BeanUtils.copyProperties(cartRecordDto, cartModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartRepository.save(cartModel));
    }

    @GetMapping
    public ResponseEntity<List<CartModel>> getAllItem(){
        List<CartModel> itemList = cartRepository.findAll();

        if(!itemList.isEmpty()) {
            for(CartModel item : itemList) {
                UUID id = item.getId();
                item.add(linkTo(methodOn(CartController.class).getOneItem(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(itemList);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneItem(@PathVariable(value="id") UUID id){
        Optional<CartModel> Item = cartRepository.findById(id);
        if(Item.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("order not found.");
        }
        Item.get().add(linkTo(methodOn(CartController.class).getAllItem()).withRel("Products List"));
        return ResponseEntity.status(HttpStatus.OK).body(Item.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable(value="id") UUID id) {
        Optional<CartModel> item = cartRepository.findById(id);
        if(item.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        cartRepository.delete(item.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateItem(@PathVariable(value="id") UUID id,
                                                @RequestBody @Valid CartRecordDTO cartRecordDto) {
        Optional<CartModel> item = cartRepository.findById(id);
        if(item.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        var itemModel = item.get();
        BeanUtils.copyProperties(cartRecordDto, itemModel);
        return ResponseEntity.status(HttpStatus.OK).body(cartRepository.save(itemModel));
    }

}
