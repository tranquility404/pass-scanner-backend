package com.hackathon.gatepass.repository;

import com.hackathon.gatepass.model.Pass;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassRepository extends MongoRepository<Pass, String> {

    Optional<Pass> findByPassCode(String passCode);

    boolean existsByPassCode(String passCode);
}
