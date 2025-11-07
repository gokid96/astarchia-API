package com.astarchia.domain.image.repository;

import com.astarchia.domain.image.entity.Image;
import com.astarchia.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByUploaderOrderByCreatedAtDesc(Users uploader);
    Optional<Image> findByStoredName(String storedName);
}