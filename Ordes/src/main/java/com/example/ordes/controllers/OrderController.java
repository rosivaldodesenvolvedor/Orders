package com.example.ordes.controllers;


import com.example.ordes.dtos.OrderRecordDTO;
import com.example.ordes.models.OrderModel;
import com.example.ordes.repositories.OrderRepository;
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
@RequestMapping("/orders")
public class OrderController {

    @Autowired
   private OrderRepository orderRepository;

    @GetMapping
    public ResponseEntity<List<OrderModel>> getAllOrders(){
        List<OrderModel> orderList = orderRepository.findAll();

        if(!orderList.isEmpty()) {
            for(OrderModel order : orderList) {
                UUID id = order.getId();
                order.add(linkTo(methodOn(OrderController.class).getOneOrder(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(orderList);
    }

    @PostMapping
    public ResponseEntity<OrderModel> saveOrder(@RequestBody @Valid OrderRecordDTO orderRecordDto) {
        var orderModel = new OrderModel();
        BeanUtils.copyProperties(orderRecordDto, orderModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderRepository.save(orderModel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneOrder(@PathVariable(value="id") UUID id){
        Optional<OrderModel> order = orderRepository.findById(id);
        if(order.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("order not found.");
        }
        order.get().add(linkTo(methodOn(OrderController.class).getAllOrders()).withRel("Products List"));
        return ResponseEntity.status(HttpStatus.OK).body(order.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable(value="id") UUID id) {
        Optional<OrderModel> order = orderRepository.findById(id);
        if(order.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        orderRepository.delete(order.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value="id") UUID id,
                                                @RequestBody @Valid OrderRecordDTO productRecordDto) {
        Optional<OrderModel> order = orderRepository.findById(id);
        if(order.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        var productModel = order.get();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.OK).body(orderRepository.save(productModel));
    }
}
