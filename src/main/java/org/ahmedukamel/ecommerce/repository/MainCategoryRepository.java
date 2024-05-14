package org.ahmedukamel.ecommerce.repository;

import org.ahmedukamel.ecommerce.model.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainCategoryRepository extends JpaRepository<MainCategory, Integer> {
}
