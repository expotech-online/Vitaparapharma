package org.ahmedukamel.ecommerce.repository;

import org.ahmedukamel.ecommerce.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
}
