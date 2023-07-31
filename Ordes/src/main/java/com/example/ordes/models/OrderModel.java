package com.example.ordes.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ORDERS")
@Getter
@Setter
public class OrderModel  extends RepresentationModel<OrderModel> implements Serializable {
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String description;

    private BigDecimal value;

    private List<String> itemsPedidos;


    public List<String> getItemsPedidos() {
        return itemsPedidos;
    }

    public void setItemsPedidos(List<String> itemsPedidos) {
        this.itemsPedidos = itemsPedidos;
    }
}
