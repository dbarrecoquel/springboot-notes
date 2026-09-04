package com.example.frontrest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import com.example.frontrest.controller.NoteController;
import com.example.frontrest.model.NoteRequest;
import com.example.note.dto.NoteDto;
import com.example.note.mapper.NoteMapper;
import com.example.note.model.Note;
import com.example.note.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(NoteController.class)
public class NoteControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@MockitoBean
	private NoteService noteService;
	
	@MockitoBean
	private NoteMapper noteMapper;
	
	private Note note;
	private NoteDto noteDto;
	private NoteRequest noteRequest;
	
	@BeforeEach
	void setUp() {
		note = new Note();
		note.setId(1L);
		note.setTitle("Titre");
		note.setContent("Contenu");
		noteDto = new NoteDto(1L, "Titre", "Contenu", LocalDateTime.now(), LocalDateTime.now());
		noteRequest = new NoteRequest("Titre", "Contenu");
	}
	
	@Test
	@DisplayName("Get /api/notes - success")
	void getAllNotes_success() throws Exception{
		
		Page<Note> page = new PageImpl<Note>(List.of(note));
		when(noteService.findWithFilters(eq("Titre"), any(Pageable.class))).thenReturn(page);
		when(noteMapper.toDtoList(any())).thenReturn(List.of(noteDto));
		
		mockMvc.perform(get("/api/notes")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("search", "Titre")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(noteDto.id()))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.last").value(true));
		
		verify(noteService).findWithFilters(eq("Titre"), any(Pageable.class));

	}
	
	@Test
	@DisplayName("Get /api/notes/{id} - success")
	void getNoteById_success() throws Exception {
		Long noteId = 1L;
		when(noteService.getNoteById(noteId)).thenReturn(noteDto);
		
		mockMvc.perform(get("/api/notes/{id}", noteId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(noteDto.id()))
			.andExpect(jsonPath("$.title").value(noteDto.title()));
		
		verify(noteService).getNoteById(noteId);
	}
	
	@Test
	@DisplayName("Post /api/notes - success")
	void createNote_success() throws Exception {
		
		when(noteService.createNote(noteRequest.title(), noteRequest.content())).thenReturn(noteDto);
		mockMvc.perform(post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(noteDto.id()))
                .andExpect(jsonPath("$.title").value(noteDto.title()));

        verify(noteService).createNote(noteRequest.title(), noteRequest.content());
		
	}
	
	@Test
	@DisplayName("Put /api/notes/{id} - success")
	void updateNote_success() throws Exception {
		
		Long noteId = 1L;
        when(noteService.updateNote(noteId, noteRequest.title(), noteRequest.content())).thenReturn(noteDto);

        mockMvc.perform(put("/api/notes/{id}", noteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(noteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(noteDto.id()))
                .andExpect(jsonPath("$.title").value(noteDto.title()));

        verify(noteService).updateNote(noteId, noteRequest.title(), noteRequest.content());
	}
	
	@Test
	@DisplayName("Delete /api/notes/{id} - success")
	void deleteNote_success() throws Exception{
		
		Long noteId = 1L;
		doNothing().when(noteService).deleteNote(noteId);
		
		mockMvc.perform(delete("/api/notes/{id}",noteId)).andExpect(status().isNoContent());
		
		verify(noteService).deleteNote(noteId);
	}
}
