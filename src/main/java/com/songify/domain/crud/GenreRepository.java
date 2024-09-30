package com.songify.domain.crud;

import com.songify.domain.crud.dto.GenreDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.Set;

interface GenreRepository extends Repository<Genre,Long> {
    Optional<Genre> findById(Long id);

    Genre save(Genre genre);

    @Modifying
    @Query("delete from Genre g where g.id = :id")
    int deleteById(Long id);

    Set<Genre> findAll();
}
