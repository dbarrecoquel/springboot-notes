package com.example.note.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.note.dto.NoteDto;
import com.example.note.model.Note;

@Mapper(componentModel = "spring")
public interface NoteMapper {
	
	NoteMapper INSTANCE = Mappers.getMapper(NoteMapper.class);
    
    NoteDto toDto(Note note);
    
    Note toEntity(NoteDto dto);
    
    List<NoteDto> toDtoList(List<Note> notes);
    
    List<Note> toEntityList(List<NoteDto> dtos);
}
