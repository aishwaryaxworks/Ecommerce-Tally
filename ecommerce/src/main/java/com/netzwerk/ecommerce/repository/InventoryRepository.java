package com.netzwerk.ecommerce.repository;

import com.netzwerk.ecommerce.entity.InventoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Integer> {

    @Query("SELECT DISTINCT i.groupName FROM InventoryEntity i WHERE i.openingBalance > 0 AND i.isDeleted = false")
    List<String> findDistinctGroupName();

    @Query("SELECT DISTINCT i.make FROM InventoryEntity i WHERE i.groupName=:groupName AND  i.openingBalance > 0 AND i.isDeleted = false")
    List<String> findDistinctMakes(@Param("groupName") String groupName);

    @Query("SELECT DISTINCT i.model FROM InventoryEntity i WHERE i.groupName=:groupName AND  i.make = :make AND i.openingBalance > 0 AND i.isDeleted = false")
    List<String> findDistinctModelsByMake(@Param("groupName") String groupName,@Param("make") String make);

    @Query("SELECT DISTINCT i.productCode FROM InventoryEntity i WHERE i.groupName=:groupName AND i.make = :make AND i.model = :model AND i.isDeleted = false  AND i.openingBalance > 0")
    List<String> findDistinctProductCodesByMakeAndModel(@Param("groupName") String groupName,@Param("make") String make, @Param("model") String model);

    @Query("SELECT i FROM InventoryEntity i WHERE i.make = :make AND i.model = :model AND i.productCode = :productCode AND i.openingBalance > 0")
    InventoryEntity findByMakeModelAndProductCode(@Param("make") String make, @Param("model") String model, @Param("productCode") String productCode);

    @Query("SELECT i FROM InventoryEntity i WHERE i.groupName=:groupName AND i.make = :make AND i.model = :model AND i.productCode = :productCode AND i.openingBalance > 0")
    InventoryEntity findByGroupNameMakeModelAndProductCode(@Param("groupName") String groupName,@Param("make") String make, @Param("model") String model, @Param("productCode") String productCode);

    @Query("SELECT i FROM InventoryEntity i WHERE i.id = :id AND i.isDeleted = false ORDER BY i.id DESC")
    InventoryEntity getById(Integer id);

    @Query("SELECT i FROM InventoryEntity i WHERE i.isDeleted = false ORDER BY i.id DESC")
    List<InventoryEntity> findByIsDeletedTrueOrderByIdDesc();

    @Query("SELECT inventory FROM InventoryEntity inventory WHERE inventory.make = :make AND inventory.model = :model AND inventory.productCode = :productCode")
    InventoryEntity findByMakeModuleAndProductCode(@Param("make") String make, @Param("model") String model, @Param("productCode") String productCode);

    @Query("SELECT DISTINCT i.make FROM InventoryEntity i WHERE LOWER(i.make) LIKE %:make%")
    List<String> suggestionByMake(@Param("make") String make);

    @Query("SELECT i FROM InventoryEntity i WHERE LOWER(i.make) LIKE %:make% AND i.isDeleted = false")
    Page<InventoryEntity> findByMakeContainingIgnoreCase(@Param("make") String make,Pageable pageable);

    Page<InventoryEntity> findByIsDeletedFalseOrderByIdDesc(Pageable pageable);

    @Query("SELECT i FROM InventoryEntity i WHERE i.isDeleted = false AND i.itemName=:itemName")
    boolean findByItemName(String itemName);

}