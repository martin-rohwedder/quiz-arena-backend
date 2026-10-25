package dk.martinrohwedder.quiz_arena_backend.repositories;

import dk.martinrohwedder.quiz_arena_backend.entities.Quiz;
import dk.martinrohwedder.quiz_arena_backend.entities.QuizStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuizRepository extends JpaRepository<Quiz, UUID> {
    List<Quiz> findAllByStatus(QuizStatus status);
}
