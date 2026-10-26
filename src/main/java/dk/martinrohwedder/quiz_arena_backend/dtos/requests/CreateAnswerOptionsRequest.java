package dk.martinrohwedder.quiz_arena_backend.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAnswerOptionsRequest(
        @NotBlank
        @Size(max = 300)
        String answerText,

        @NotNull
        @Min(1)
        Integer optionOrder,

        boolean isCorrect
) {}
