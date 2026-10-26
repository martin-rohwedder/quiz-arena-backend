package dk.martinrohwedder.quiz_arena_backend.dtos.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateQuestionRequest(
        @NotBlank
        @Size(max = 500)
        String questionText,

        @Min(1)
        int questionOrder,

        @Min(1)
        int timeLimitSeconds,

        @NotEmpty
        List<@Valid CreateAnswerOptionsRequest> answerOptions
) {}
