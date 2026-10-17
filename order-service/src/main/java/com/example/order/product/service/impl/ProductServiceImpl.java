package com.example.order.product.service.impl;

import com.example.order.product.dto.ProductCreateRequest;
import com.example.order.product.dto.ProductUpdateRequest;
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

    @Override
    public Product create(ProductCreateRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setStatus(1);
        product.setVersion(0L);
        productMapper.insert(product);
        return product;
    }

    @Override
    public Product update(Long id, ProductUpdateRequest request) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            return null;
        }
        product.setName(request.name());
        product.setPrice(request.price());
        product.setStock(request.stock());
        productMapper.updateById(product);
        return productMapper.selectById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            return;
        }
        product.setStatus(status);
        productMapper.updateById(product);
    }

    @Override
    public void delete(Long id) {
        productMapper.deleteById(id);
    }
}
