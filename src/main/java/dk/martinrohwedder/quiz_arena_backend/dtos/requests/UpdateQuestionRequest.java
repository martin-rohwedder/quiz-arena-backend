package dk.martinrohwedder.quiz_arena_backend.dtos.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record UpdateQuestionRequest(
        @NotBlank
        @Size(max = 500)
        String questionText,

        @Min(1)
        int questionOrder,

        @Min(1)
        @Max(60)
        int timeLimitSeconds,

        @NotEmpty
        List<@Valid UpdateAnswerOptionsRequest> answerOptions
) {}
