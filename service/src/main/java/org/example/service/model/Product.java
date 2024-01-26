package org.example.service.model;

import org.example.service.model.enums.ProductStatus;
import org.example.service.model.enums.ProductStatusAtSeller;

import java.util.UUID;

public class Product {
    private UUID id = UUID.randomUUID();
    private String name;
    private ProductStatus productStatus;
    private ProductStatusAtSeller productStatusAtSeller;        // changing because of the buttons

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public ProductStatusAtSeller getProductStatusAtSeller() {
        return productStatusAtSeller;
    }

    public void setProductStatusAtSeller(ProductStatusAtSeller productStatusAtSeller) {
        this.productStatusAtSeller = productStatusAtSeller;
    }
}
