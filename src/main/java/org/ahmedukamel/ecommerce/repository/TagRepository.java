package org.ahmedukamel.ecommerce.repository;

import org.ahmedukamel.ecommerce.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
}