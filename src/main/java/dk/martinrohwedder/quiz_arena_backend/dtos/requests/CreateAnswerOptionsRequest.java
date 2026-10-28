package dk.martinrohwedder.quiz_arena_backend.dtos.requests;

import jakarta.validation.constraints.*;

public record CreateAnswerOptionsRequest(
        @NotBlank
        @Size(max = 300)
        String answerText,

        @NotNull
        @Min(1)
        @Max(4)
        Integer optionOrder,

        boolean isCorrect
) implements AnswerOptionRequest {}
