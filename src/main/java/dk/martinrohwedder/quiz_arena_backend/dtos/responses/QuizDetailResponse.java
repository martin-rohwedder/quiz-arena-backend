package dk.martinrohwedder.quiz_arena_backend.dtos.responses;

import dk.martinrohwedder.quiz_arena_backend.entities.QuizStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record QuizDetailResponse(
        UUID id,
        String title,
        String description,
        QuizStatus status,
        List<QuestionResponse> questions,
        Instant createdAt,
        Instant updatedAt
) {}
