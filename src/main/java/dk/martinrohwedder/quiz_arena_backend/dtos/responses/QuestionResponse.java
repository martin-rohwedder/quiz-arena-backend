package dk.martinrohwedder.quiz_arena_backend.dtos.responses;

import java.util.List;
import java.util.UUID;

public record QuestionResponse(
        UUID id,
        String questionText,
        int questionOrder,
        int timeLimitSeconds,
        List<AnswerOptionResponse> answerOptions
) {}
