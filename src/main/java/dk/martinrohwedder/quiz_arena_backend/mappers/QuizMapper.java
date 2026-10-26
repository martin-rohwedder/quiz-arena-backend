package dk.martinrohwedder.quiz_arena_backend.mappers;

import dk.martinrohwedder.quiz_arena_backend.dtos.responses.QuizDetailResponse;
import dk.martinrohwedder.quiz_arena_backend.dtos.responses.QuizResponse;
import dk.martinrohwedder.quiz_arena_backend.entities.Quiz;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = QuestionMapper.class
)
public interface QuizMapper {
    QuizResponse toDto(Quiz quiz);
    QuizDetailResponse toDetailDto(Quiz quiz);
}
