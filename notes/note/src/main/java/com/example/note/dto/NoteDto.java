package com.example.note.dto;

import java.time.LocalDateTime;

public record NoteDto(Long id,
		 			  String title,
		 			  String content,
		 			  LocalDateTime createdAt,
		 			  LocalDateTime updatedAt) {

	public static NoteDto from(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
		
		return new NoteDto(id, title, content, createdAt, updatedAt);
		
	}
}
