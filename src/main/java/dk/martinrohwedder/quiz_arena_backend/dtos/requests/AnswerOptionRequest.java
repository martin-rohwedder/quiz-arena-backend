package dk.martinrohwedder.quiz_arena_backend.dtos.requests;

public interface AnswerOptionRequest {
        Integer optionOrder();
        boolean isCorrect();
}
