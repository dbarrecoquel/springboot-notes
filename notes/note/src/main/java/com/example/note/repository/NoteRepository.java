package com.example.note.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.note.model.Note;

public interface NoteRepository extends JpaRepository<Note, Long>{
	Optional<Note> findByTitle(String title);
}
