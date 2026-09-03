package com.example.note;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.note.dto.NoteDto;
import com.example.note.mapper.NoteMapper;
import com.example.note.model.Note;
import com.example.note.repository.NoteRepository;
import com.example.note.service.NoteService;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {
	@Mock
	private NoteRepository noteRepository;
	
	@Mock
	private NoteMapper noteMapper;
	
	@InjectMocks
	private NoteService noteService;
	
	private Note note;
	private NoteDto noteDto;
	
	@BeforeEach
	void setUp() {
		
		note = new Note();
		note.setId(1L);
		note.setTitle("Title");
		note.setContent("TestContent");
		
		noteDto = NoteDto.from(1L, "Title", "TitleContent", LocalDateTime.now(), LocalDateTime.now());
		
	}
	
	@Nested
	@DisplayName("Recherches simples")
	class FindTests {
		
		@Test
		@DisplayName("shouldGetAllNote pageable")
		void shouldGetAllNotePageable() {
			
			Pageable pageable = PageRequest.of(0, 10);
			Page<Note> page = new PageImpl<>(List.of(note));
			
			when(noteRepository.findAll(pageable)).thenReturn(page);
			
			Page<Note> result = noteService.getAllNotes(pageable);
			

	        assertThat(result.getContent()).hasSize(1);
	        verify(noteRepository, times(1)).findAll(pageable);
		}
		@Test
		@DisplayName("shouldGetAllNote list")
		void shouldGetAllNotelist() {
			
			List<Note> notes = List.of(note);
			List<NoteDto> notesDto = List.of(noteDto);
			
			when(noteRepository.findAll()).thenReturn(notes);
			when(noteMapper.toDtoList(notes)).thenReturn(notesDto);
			
			List<NoteDto> result = noteService.getAllNotes();
			

	        assertThat(result).hasSize(1);
	        verify(noteRepository, times(1)).findAll();
		}
		
		@Test
		@DisplayName("shouldGetNoteById")
		void shouldGetNoteById() {
			
			
			when(noteRepository.findById(1L)).thenReturn(Optional.of(note));
			when(noteMapper.toDto(note)).thenReturn(noteDto);
			
			NoteDto result = noteService.getNoteById(1L);
			

	        assertThat(result).isNotNull();
	        assertThat(result.title()).isEqualTo("Title");
	        verify(noteRepository, times(1)).findById(1L);
	        
	        
		}
		@Test
		@DisplayName("shouldGetNoteByTitle")
		void shouldGetNoteByTitle() {
			
			
			when(noteRepository.findByTitle("Title")).thenReturn(Optional.of(note));
			when(noteMapper.toDto(note)).thenReturn(noteDto);
			
			NoteDto result = noteService.findByTitle("Title");
			

	        assertThat(result).isNotNull();
	        assertThat(result.title()).isEqualTo("Title");
	        verify(noteRepository, times(1)).findByTitle("Title");
	        
	        
		}
	}
	
	@Nested
	@DisplayName("Save test")
	class SaveTests {
		
		@Test
		@DisplayName("ShoudlCreateNote")
		void shouldCreateNote() {
			
			when(noteRepository.save(any(Note.class))).thenReturn(note);
			when(noteMapper.toDto(note)).thenReturn(noteDto);
			
			NoteDto result = noteService.createNote("Title", "TitleContent");
			
			assertThat(result).isEqualTo(noteDto);
            verify(noteRepository).save(any(Note.class));
		}
		@Test
		@DisplayName("ShoudlUpdateNote")
		void shouldUpdateNote() {
			
			when(noteRepository.findById(1L)).thenReturn(Optional.of(note));
			when(noteRepository.save(any(Note.class))).thenReturn(note);
			when(noteMapper.toDto(note)).thenReturn(noteDto);
			
			NoteDto result = noteService.updateNote(1L,"Title", "TitleContent");
			
			assertThat(result).isEqualTo(noteDto);
            verify(noteRepository).save(any(Note.class));
		}
	}
	
	@Nested
	@DisplayName("Remove tests")
	class RemoveTests{
		
		@Test
		@DisplayName("should remove note")
		void shouldRemoveAddress() {
			noteService.deleteNote(1L);
			
			verify(noteRepository, times(1)).deleteById(1L);
		}
		
	}
}
