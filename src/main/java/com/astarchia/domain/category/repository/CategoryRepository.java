package com.astarchia.domain.category.repository;

import com.astarchia.domain.category.entity.Category;
import com.astarchia.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByOwnerOrderByDisplayOrderAsc(Users owner);
    Optional<Category> findByOwnerAndSlug(Users owner, String slug);
    boolean existsByOwnerAndName(Users owner, String name);
}