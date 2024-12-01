package com.fitable.backend.video.repository;

import com.fitable.backend.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    @Query("SELECT v.url FROM Video v WHERE v.title LIKE %:title%")
    List<String> findUrlsByTitleContaining(String title);
}
