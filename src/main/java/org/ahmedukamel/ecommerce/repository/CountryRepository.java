package org.ahmedukamel.ecommerce.repository;

import org.ahmedukamel.ecommerce.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
}
