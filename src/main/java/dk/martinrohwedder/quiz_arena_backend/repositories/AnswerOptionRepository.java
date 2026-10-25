package dk.martinrohwedder.quiz_arena_backend.repositories;

import dk.martinrohwedder.quiz_arena_backend.entities.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AnswerOptionRepository extends JpaRepository<AnswerOption, UUID> {
    List<AnswerOption> findAllByQuestionIdOrderByOptionOrderAsc(UUID questionId);
}
