package com.gessal.PartsList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface PartRepository extends CrudRepository<Part, Integer> {
    Optional<Part> findById(Integer id);
    List<Part> findByName(String name);
    Page<Part> findAll(Pageable pageable);
}
