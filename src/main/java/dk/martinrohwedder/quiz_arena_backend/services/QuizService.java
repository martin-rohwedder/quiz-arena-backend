package dk.martinrohwedder.quiz_arena_backend.services;

import dk.martinrohwedder.quiz_arena_backend.dtos.requests.CreateQuizRequest;
import dk.martinrohwedder.quiz_arena_backend.dtos.requests.UpdateQuizRequest;
import dk.martinrohwedder.quiz_arena_backend.dtos.responses.QuizDetailResponse;
import dk.martinrohwedder.quiz_arena_backend.dtos.responses.QuizResponse;
import dk.martinrohwedder.quiz_arena_backend.entities.*;
import dk.martinrohwedder.quiz_arena_backend.mappers.QuizMapper;
import dk.martinrohwedder.quiz_arena_backend.repositories.QuizRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizMapper quizMapper;

    public QuizResponse createQuiz(CreateQuizRequest request) {
        Quiz quiz = Quiz.builder()
                .title(request.title())
                .description(request.description())
                .status(QuizStatus.DRAFT)
                .build();

        return quizMapper.toDto(quizRepository.save(quiz));
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<QuizResponse> getAllQuizzes() {
        return quizRepository.findAll()
                .stream()
                .map(quizMapper::toDto)
                .toList();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public QuizDetailResponse getQuizById(UUID id) {
        Quiz quiz = findQuizOrThrow(id);
        return quizMapper.toDetailDto(quiz);
    }

    public QuizResponse updateQuiz(UUID id, UpdateQuizRequest request) {
        Quiz quiz = findQuizOrThrow(id);

        quiz.setTitle(request.title());
        quiz.setDescription(request.description());

        return quizMapper.toDto(quizRepository.save(quiz));
    }

    public QuizResponse publishQuiz(UUID id) {
        Quiz quiz = findQuizOrThrow(id);

        validateQuizForPublishing(quiz);

        quiz.setStatus(QuizStatus.PUBLISHED);

        return quizMapper.toDto(quizRepository.save(quiz));
    }

    public void deleteQuiz(UUID id) {
        Quiz quiz = findQuizOrThrow(id);
        quizRepository.delete(quiz);
    }

    private Quiz findQuizOrThrow(UUID id) {
        return quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Quiz not found: " + id));
    }

    private void validateQuizForPublishing(Quiz quiz) {
        if (quiz.getQuestions().isEmpty()) {
            throw new IllegalStateException("A quiz must contain at least one question.");
        }

        for (Question question : quiz.getQuestions()) {
            List<AnswerOption> options = question.getAnswerOptions();

            if (options.size() < 2) {
                throw new IllegalStateException("Each question must have at least two answer options.");
            }

            long correctAnswers = options.stream()
                    .filter(option -> Boolean.TRUE.equals(option.getIsCorrect()))
                    .count();

            if (correctAnswers != 1) {
                throw new IllegalStateException("Each question must have exactly one correct answer.");
            }
        }
    }
}
