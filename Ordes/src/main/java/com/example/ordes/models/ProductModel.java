package com.example.ordes.springboot.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.*;


import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;


@Entity
@Table(name = "TB_PRODUCTS")
@Getter
@Setter
public class ProductModel extends RepresentationModel<ProductModel> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private UUID idProduct;
	private String name;
	private BigDecimal value;

}
