package dk.martinrohwedder.quiz_arena_backend.controllers;

import dk.martinrohwedder.quiz_arena_backend.dtos.requests.CreateGameRoomRequest;
import dk.martinrohwedder.quiz_arena_backend.dtos.responses.GameRoomResponse;
import dk.martinrohwedder.quiz_arena_backend.services.GameRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/game-rooms")
@RequiredArgsConstructor
public class GameRoomController {
    private final GameRoomService gameRoomService;

    @PostMapping
    public ResponseEntity<GameRoomResponse> createGameRoom(@Valid @RequestBody CreateGameRoomRequest request) {
        GameRoomResponse response = gameRoomService.createGameRoom(request);
        var uriLocation = UriComponentsBuilder.fromPath("/api/game-rooms/code/{roomCode}").buildAndExpand(response.roomCode()).toUri();

        return ResponseEntity.created(uriLocation).body(response);
    }

    @GetMapping("/code/{roomCode}")
    public ResponseEntity<GameRoomResponse> getGameRoomByCode(@PathVariable String roomCode) {
        return ResponseEntity.ok(gameRoomService.getGameRoomByCode(roomCode));
    }
}
