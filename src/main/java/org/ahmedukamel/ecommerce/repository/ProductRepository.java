package org.ahmedukamel.ecommerce.repository;

import org.ahmedukamel.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByCategory_MainCategory_CategoryId(Integer mainCategoryId);

    List<Product> findAllByActive(Boolean active);

    List<Product> findAllByCategory_CategoryId(Integer category);
}
