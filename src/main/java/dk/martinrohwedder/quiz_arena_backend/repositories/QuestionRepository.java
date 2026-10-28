package dk.martinrohwedder.quiz_arena_backend.repositories;

import dk.martinrohwedder.quiz_arena_backend.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findAllByQuiz_IdOrderByQuestionOrderAsc(UUID quizId);
    boolean existsByQuiz_IdAndQuestionOrder(UUID quizId, Integer questionOrder);
    Optional<Question> findByIdAndQuiz_Id(UUID questionId, UUID quizId);
}
