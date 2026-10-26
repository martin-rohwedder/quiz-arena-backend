package dk.martinrohwedder.quiz_arena_backend.dtos.responses;

import java.util.UUID;

public record AnswerOptionResponse(
        UUID id,
        String answerText,
        int optionOrder,
        boolean isCorrect
) {}
