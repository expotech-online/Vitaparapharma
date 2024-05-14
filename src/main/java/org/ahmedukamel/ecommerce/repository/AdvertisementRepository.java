package org.ahmedukamel.ecommerce.repository;

import org.ahmedukamel.ecommerce.model.Advertisement;
import org.ahmedukamel.ecommerce.model.enumeration.AdvertisementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {
    List<Advertisement> findAllByStatus(AdvertisementStatus status);
}
