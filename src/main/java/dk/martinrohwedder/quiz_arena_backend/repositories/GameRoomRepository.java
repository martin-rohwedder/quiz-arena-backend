package dk.martinrohwedder.quiz_arena_backend.repositories;

import dk.martinrohwedder.quiz_arena_backend.entities.GameRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GameRoomRepository extends JpaRepository<GameRoom, UUID> {
    Optional<GameRoom> findByRoomCode(String roomCode);
    boolean existsByRoomCode(String roomCode);
}
