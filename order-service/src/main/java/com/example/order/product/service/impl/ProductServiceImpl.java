package com.example.order.product.service.impl;

import com.example.order.product.entity.Product;
import com.example.order.product.mapper.ProductMapper;
import com.example.order.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public Product getById(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public List<Product> listAll() {
        return productMapper.selectList(null);
    }
}
