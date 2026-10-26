package dk.martinrohwedder.quiz_arena_backend.controllers;

import dk.martinrohwedder.quiz_arena_backend.dtos.requests.CreateQuizRequest;
import dk.martinrohwedder.quiz_arena_backend.dtos.requests.UpdateQuizRequest;
import dk.martinrohwedder.quiz_arena_backend.dtos.responses.QuizDetailResponse;
import dk.martinrohwedder.quiz_arena_backend.dtos.responses.QuizResponse;
import dk.martinrohwedder.quiz_arena_backend.services.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<QuizResponse> createQuiz(@Valid @RequestBody CreateQuizRequest request) {
        QuizResponse response = quizService.createQuiz(request);
        var uriLocation = UriComponentsBuilder.fromPath("/api/quizzes/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uriLocation).body(response);
    }

    @GetMapping
    public ResponseEntity<List<QuizResponse>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDetailResponse> getQuizById(@PathVariable UUID id) {
        return ResponseEntity.ok(quizService.getQuizById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizResponse> updateQuiz(@PathVariable UUID id, @Valid @RequestBody UpdateQuizRequest request) {
        return ResponseEntity.ok(quizService.updateQuiz(id, request));
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<QuizResponse> publishQuiz(@PathVariable UUID id) {
        return ResponseEntity.ok(quizService.publishQuiz(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable UUID id) {
        quizService.deleteQuiz(id);

        return ResponseEntity.noContent().build();
    }
}
