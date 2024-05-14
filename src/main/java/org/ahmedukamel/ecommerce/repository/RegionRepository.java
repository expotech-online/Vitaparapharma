package org.ahmedukamel.ecommerce.repository;

import org.ahmedukamel.ecommerce.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {
}
