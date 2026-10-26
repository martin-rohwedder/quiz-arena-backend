package dk.martinrohwedder.quiz_arena_backend.dtos.responses;

import dk.martinrohwedder.quiz_arena_backend.entities.QuizStatus;

import java.time.Instant;
import java.util.UUID;

public record QuizResponse(
        UUID id,
        String title,
        String description,
        QuizStatus status,
        Instant createdAt,
        Instant updatedAt
) {}
