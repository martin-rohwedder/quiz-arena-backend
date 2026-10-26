package dk.martinrohwedder.quiz_arena_backend.mappers;

import dk.martinrohwedder.quiz_arena_backend.dtos.responses.PlayerQuestionResponse;
import dk.martinrohwedder.quiz_arena_backend.dtos.responses.QuestionResponse;
import dk.martinrohwedder.quiz_arena_backend.entities.Question;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = AnswerOptionMapper.class
)
public interface QuestionMapper {
    QuestionResponse toDto(Question question);
    PlayerQuestionResponse toPlayerDto(Question question);
}
