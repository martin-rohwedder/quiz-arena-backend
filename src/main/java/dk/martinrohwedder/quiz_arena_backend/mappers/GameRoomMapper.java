package dk.martinrohwedder.quiz_arena_backend.mappers;

import dk.martinrohwedder.quiz_arena_backend.dtos.responses.GameRoomResponse;
import dk.martinrohwedder.quiz_arena_backend.entities.GameRoom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameRoomMapper {
    GameRoomResponse toDto(GameRoom gameRoom);
}
