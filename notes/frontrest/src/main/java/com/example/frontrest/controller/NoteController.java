package com.example.frontrest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.frontrest.model.NoteRequest;
import com.example.note.dto.NoteDto;
import com.example.note.mapper.NoteMapper;
import com.example.note.model.Note;
import com.example.note.service.NoteService;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class NoteController {

	private final NoteService noteService;
	private final NoteMapper noteMapper;
	
	public NoteController(NoteService noteService, NoteMapper noteMapper) {
		
		this.noteService = noteService;
		this.noteMapper = noteMapper;
	}
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllNote(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "id") String sortBy,
	        @RequestParam(defaultValue = "asc") String direction,
	        @RequestParam(required = false) String title) {

	    Sort sort = direction.equalsIgnoreCase("desc")
	            ? Sort.by(sortBy).descending()
	            : Sort.by(sortBy).ascending();

	    Pageable pageable = PageRequest.of(page, size, sort);

	    Page<Note> pageResult = noteService.findWithFilters(title, pageable);

	    List<NoteDto> content = noteMapper.toDtoList(pageResult.getContent());
	            

	    Map<String, Object> response = new HashMap<>();
	    response.put("content", content);
	    response.put("page", pageResult.getNumber());
	    response.put("size", pageResult.getSize());
	    response.put("totalElements", pageResult.getTotalElements());
	    response.put("totalPages", pageResult.getTotalPages());
	    response.put("last", pageResult.isLast());

	    return ResponseEntity.ok(response);
	}
	@GetMapping("/{id}")
	public ResponseEntity<NoteDto> getAdsById(@PathVariable Long id) {
		
		return ResponseEntity.ok(noteService.getNoteById(id));
		
	}
	
	@PostMapping
	public ResponseEntity<NoteDto> createNote(@RequestBody NoteRequest body){
		
		NoteDto note = noteService.createNote(body.title(),body.content());
		return ResponseEntity.ok(note);
		
	}
	@PutMapping("/{id}")
	public ResponseEntity<NoteDto> updateNote(@PathVariable Long id, @RequestBody NoteRequest body){
		
		NoteDto note = noteService.updateNote(id, body.title(),body.content());
		return ResponseEntity.ok(note);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
		
		noteService.deleteNote(id);
		return ResponseEntity.noContent().build();
	}
}
