package dk.martinrohwedder.quiz_arena_backend.controllers;

import dk.martinrohwedder.quiz_arena_backend.dtos.requests.CreateQuestionRequest;
import dk.martinrohwedder.quiz_arena_backend.dtos.responses.QuestionResponse;
import dk.martinrohwedder.quiz_arena_backend.services.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/quizzes/{quizId}/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(@PathVariable UUID quizId, @Valid @RequestBody CreateQuestionRequest request, UriComponentsBuilder uriBuilder) {
        QuestionResponse response = questionService.createQuestion(quizId, request);
        var location = uriBuilder.path("/api/quizzes/{quizId}/questions/{questionId}").buildAndExpand(quizId, response.id()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<QuestionResponse>> getQuestions(@PathVariable UUID quizId) {
        return ResponseEntity.ok(questionService.getQuestions(quizId));
    }
}
