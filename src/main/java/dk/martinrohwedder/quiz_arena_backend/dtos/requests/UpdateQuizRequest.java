package dk.martinrohwedder.quiz_arena_backend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateQuizRequest(
        @NotBlank
        @Size(max = 100)
        String title,

        String description
) {}
