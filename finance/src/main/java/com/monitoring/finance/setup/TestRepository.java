package com.monitoring.finance.setup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestRepository extends JpaRepository<TestEntity, Integer> {

    @Query("SELECT t FROM TestEntity t WHERE t.column1 = ?1")
    List<TestEntity> findByColumn1(String column1Value); //JPQL

    @Query(value = "SELECT * FROM test_entity WHERE column1 = :column1Value", nativeQuery = true)
    List<TestEntity> findByColumn1Native(String column1Value); //SQL


}
