package dk.martinrohwedder.quiz_arena_backend.dtos.responses;

import java.util.List;
import java.util.UUID;

public record PlayerQuestionResponse(
        UUID id,
        String questionText,
        int timeLimitSeconds,
        List<PlayerAnswerOptionResponse> answerOptions
) {}
