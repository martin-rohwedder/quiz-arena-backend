package dk.martinrohwedder.quiz_arena_backend.services;

import dk.martinrohwedder.quiz_arena_backend.dtos.requests.CreateAnswerOptionsRequest;
import dk.martinrohwedder.quiz_arena_backend.dtos.requests.CreateQuestionRequest;
import dk.martinrohwedder.quiz_arena_backend.dtos.responses.QuestionResponse;
import dk.martinrohwedder.quiz_arena_backend.entities.AnswerOption;
import dk.martinrohwedder.quiz_arena_backend.entities.Question;
import dk.martinrohwedder.quiz_arena_backend.entities.Quiz;
import dk.martinrohwedder.quiz_arena_backend.entities.QuizStatus;
import dk.martinrohwedder.quiz_arena_backend.mappers.QuestionMapper;
import dk.martinrohwedder.quiz_arena_backend.repositories.QuestionRepository;
import dk.martinrohwedder.quiz_arena_backend.repositories.QuizRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;
    private final QuestionMapper questionMapper;

    public QuestionResponse createQuestion(UUID quizId, CreateQuestionRequest request) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found: " + quizId));

        if (quiz.getStatus() != QuizStatus.DRAFT) {
            throw new IllegalStateException("Questions can only be added to draft quizzes.");
        }

        validateAnswerOptions(request.answerOptions());

        boolean orderAlreadyExists = questionRepository.findAllByQuiz_IdOrderByQuestionOrderAsc(quizId)
                .stream()
                .anyMatch(question -> question.getQuestionOrder() == request.questionOrder());

        if (orderAlreadyExists) {
            throw new IllegalStateException("A question with this order already exists in the quiz.");
        }

        Question question = Question.builder()
                .questionText(request.questionText())
                .questionOrder(request.questionOrder())
                .timeLimitSeconds(request.timeLimitSeconds())
                .build();

        quiz.addQuestion(question);

        for (CreateAnswerOptionsRequest optionRequest : request.answerOptions()) {
            AnswerOption option = AnswerOption.builder()
                    .answerText(optionRequest.answerText())
                    .optionOrder(optionRequest.optionOrder())
                    .isCorrect(optionRequest.isCorrect())
                    .build();

            question.addAnswerOption(option);
        }

        Question savedQuestion = questionRepository.save(question);

        return questionMapper.toDto(savedQuestion);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestions(UUID quizId) {
        if (!quizRepository.existsById(quizId)) {
            throw new EntityNotFoundException("Quiz not found: " + quizId);
        }

        return questionRepository.findAllByQuiz_IdOrderByQuestionOrderAsc(quizId)
                .stream()
                .map(questionMapper::toDto)
                .toList();
    }

    private void validateAnswerOptions(List<CreateAnswerOptionsRequest> answerOptions) {
        if (answerOptions.size() < 2) {
            throw new IllegalStateException("A question must have at least two answer options.");
        }

        long correctAnswers = answerOptions.stream()
                .filter(CreateAnswerOptionsRequest::isCorrect)
                .count();

        if (correctAnswers != 1) {
            throw new IllegalStateException("A question must have exactly one correct answer.");
        }

        long distinctOrders = answerOptions.stream()
                .map(CreateAnswerOptionsRequest::optionOrder)
                .distinct()
                .count();

        if (distinctOrders != answerOptions.size()) {
            throw new IllegalStateException("Answer option orders must be unique.");
        }
    }
}

