package dk.martinrohwedder.quiz_arena_backend.dtos.responses;

import java.util.UUID;

public record PlayerAnswerOptionResponse(
        UUID id,
        String answerText,
        int optionOrder
) {}
