package dk.martinrohwedder.quiz_arena_backend.mappers;

import dk.martinrohwedder.quiz_arena_backend.dtos.responses.AnswerOptionResponse;
import dk.martinrohwedder.quiz_arena_backend.dtos.responses.PlayerAnswerOptionResponse;
import dk.martinrohwedder.quiz_arena_backend.entities.AnswerOption;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnswerOptionMapper {
    AnswerOptionResponse toDto(AnswerOption answerOption);
    PlayerAnswerOptionResponse toPlayerDto(AnswerOption answerOption);
}
