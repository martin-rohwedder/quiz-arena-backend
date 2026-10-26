package dk.martinrohwedder.quiz_arena_backend.services;

import dk.martinrohwedder.quiz_arena_backend.dtos.requests.CreateGameRoomRequest;
import dk.martinrohwedder.quiz_arena_backend.dtos.responses.GameRoomResponse;
import dk.martinrohwedder.quiz_arena_backend.entities.GameRoom;
import dk.martinrohwedder.quiz_arena_backend.entities.GameRoomStatus;
import dk.martinrohwedder.quiz_arena_backend.entities.Quiz;
import dk.martinrohwedder.quiz_arena_backend.entities.QuizStatus;
import dk.martinrohwedder.quiz_arena_backend.mappers.GameRoomMapper;
import dk.martinrohwedder.quiz_arena_backend.repositories.GameRoomRepository;
import dk.martinrohwedder.quiz_arena_backend.repositories.QuizRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class GameRoomService {
    private static final String ROOM_CODE_CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int ROOM_CODE_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final GameRoomRepository gameRoomRepository;
    private final QuizRepository quizRepository;
    private final GameRoomMapper gameRoomMapper;

    public GameRoomResponse createGameRoom(CreateGameRoomRequest request) {
        Quiz quiz = quizRepository.findById(request.quizId()).orElseThrow(() -> new EntityNotFoundException("Quiz not found: " + request.quizId()));

        if (quiz.getStatus() != QuizStatus.PUBLISHED) {
            throw new IllegalStateException("Only published quizzes can be played.");
        }

        GameRoom gameRoom = GameRoom.builder()
                .roomCode(generateUniqueRoomCode())
                .quiz(quiz)
                .status(GameRoomStatus.WAITING)
                .build();

        return gameRoomMapper.toDto(gameRoomRepository.save(gameRoom));
    }

    @Transactional(readOnly = true)
    public GameRoomResponse getGameRoomByCode(String roomCode) {
        GameRoom gameRoom = gameRoomRepository.findByRoomCode(roomCode.toUpperCase()).orElseThrow(() -> new EntityNotFoundException("Game room not found: " + roomCode));

        return gameRoomMapper.toDto(gameRoom);
    }

    private String generateUniqueRoomCode() {
        String roomCode;

        do {
            StringBuilder builder = new StringBuilder(ROOM_CODE_LENGTH);

            for (int i = 0; i < ROOM_CODE_LENGTH; i++) {
                int index = RANDOM.nextInt(ROOM_CODE_CHARACTERS.length());

                builder.append(ROOM_CODE_CHARACTERS.charAt(index));
            }

            roomCode = builder.toString();

        } while (gameRoomRepository.existsByRoomCode(roomCode));

        return roomCode;
    }
}
