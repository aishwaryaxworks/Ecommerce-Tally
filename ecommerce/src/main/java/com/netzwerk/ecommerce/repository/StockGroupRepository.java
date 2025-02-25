package com.netzwerk.ecommerce.repository;

import com.netzwerk.ecommerce.entity.StockGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockGroupRepository extends JpaRepository<StockGroupEntity, Integer> {
    @Query("SELECT DISTINCT s.name FROM StockGroupEntity s")
    List<String> findAllGroupNames();
    boolean existsByName(String name);

}