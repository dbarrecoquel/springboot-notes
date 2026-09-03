package com.example.note.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.note.model.Note;

public interface NoteRepository extends JpaRepository<Note, Long>{
	Optional<Note> findByTitle(String title);
	
	@Query("""
    	    SELECT n FROM Note n
    	    WHERE (:title IS NULL OR LOWER(n.title) LIKE LOWER(CONCAT('%', CAST(:title AS string), '%'))
    	                           OR LOWER(n.content) LIKE LOWER(CONCAT('%', CAST(:title AS string), '%')))
    	""")
    Page<Note> findWithFilters(
        @Param("title") String title,
        Pageable pageable
    );
}
