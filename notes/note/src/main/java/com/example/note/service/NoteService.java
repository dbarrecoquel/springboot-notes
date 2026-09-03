package com.example.note.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.note.dto.NoteDto;
import com.example.note.mapper.NoteMapper;
import com.example.note.model.Note;
import com.example.note.repository.NoteRepository;

@Service
@Transactional
public class NoteService {

	private final NoteRepository noteRepository;
	private final NoteMapper noteMapper;
	
	public NoteService(NoteRepository noteRepository, NoteMapper noteMapper) {
		this.noteRepository = noteRepository;
		this.noteMapper = noteMapper;
	}
	
	public Page<Note> getAllNotes(Pageable pageable) {
		
		return noteRepository.findAll(pageable);
		
	}
	
	public List<NoteDto> getAllNotes(){
		
		return noteMapper.toDtoList(noteRepository.findAll());
	}
	
	public NoteDto getNoteById(Long id) {
		
		Note note = noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Unable to find Note"));
		
		return noteMapper.toDto(note);
	}
	
	public void deleteNote(Long id) {
		
		noteRepository.deleteById(id);
	}
	
	public NoteDto createNote(String title, String content) {
		
		Note note = new Note();
		note.setTitle(title);
		note.setContent(content);
		note.setCreatedAt(LocalDateTime.now());
		note.setUpdatedAt(LocalDateTime.now());
		
		Note saved = noteRepository.save(note);
		
		return noteMapper.toDto(saved);
		
	}
	
	public NoteDto updateNote(Long id , String title, String content) {
		

		Note note = noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Unable to find Note"));
		
		note.setTitle(title);
		note.setContent(content);
		note.setUpdatedAt(LocalDateTime.now());
		
		Note saved = noteRepository.save(note);
		
		return noteMapper.toDto(saved);
	}
	
	public NoteDto findByTitle(String title) {
		

		Note note = noteRepository.findByTitle(title).orElseThrow(() -> new RuntimeException("Unable to find Note"));
		
		return noteMapper.toDto(note);
	}
}
