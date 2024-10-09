package com.squarecross.photoalbum.repository;

import com.squarecross.photoalbum.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findByAlbumNameContainingOrderByCreatedAtDesc(String keyword);

    List<Album> findByAlbumNameContainingOrderByAlbumNameAsc(String keyword);
}
