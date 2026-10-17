package com.example.order.product.service;

import com.example.order.product.dto.ProductCreateRequest;
import com.example.order.product.dto.ProductUpdateRequest;
import com.example.order.product.entity.Product;

import java.util.List;

public interface ProductService {

    Product getById(Long id);

    List<Product> listAll();

    Product create(ProductCreateRequest request);

    Product update(Long id, ProductUpdateRequest request);

    void updateStatus(Long id, Integer status);

    void delete(Long id);
}
