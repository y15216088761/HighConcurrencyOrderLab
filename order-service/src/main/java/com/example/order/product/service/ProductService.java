package com.example.order.product.service;

import com.example.order.product.entity.Product;

import java.util.List;

public interface ProductService {

    Product getById(Long id);

    List<Product> listAll();
}
