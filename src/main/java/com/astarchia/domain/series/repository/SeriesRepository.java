package com.astarchia.domain.series.repository;

import com.astarchia.domain.series.entity.Series;
import com.astarchia.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {
    List<Series> findByOwner(Users owner);
    Optional<Series> findByOwnerAndSlug(Users owner, String slug);
    boolean existsByOwnerAndName(Users owner, String name);
}