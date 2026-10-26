package dk.martinrohwedder.quiz_arena_backend.dtos.responses;

import dk.martinrohwedder.quiz_arena_backend.entities.GameRoomStatus;

import java.time.Instant;
import java.util.UUID;

public record GameRoomResponse(
        UUID id,
        String roomCode,
        GameRoomStatus status,
        Instant createdAt
) {}
