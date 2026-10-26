package dk.martinrohwedder.quiz_arena_backend.dtos.requests;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateGameRoomRequest(
        @NotNull
        UUID quizId
) {}
