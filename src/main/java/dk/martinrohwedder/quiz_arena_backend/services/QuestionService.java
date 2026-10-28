package dk.martinrohwedder.quiz_arena_backend.services;

import dk.martinrohwedder.quiz_arena_backend.dtos.requests.AnswerOptionRequest;
import dk.martinrohwedder.quiz_arena_backend.dtos.requests.CreateAnswerOptionsRequest;
import dk.martinrohwedder.quiz_arena_backend.dtos.requests.CreateQuestionRequest;
import dk.martinrohwedder.quiz_arena_backend.dtos.requests.UpdateQuestionRequest;
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

        if (questionRepository.existsByQuiz_IdAndQuestionOrder(quizId, request.questionOrder())) {
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

    @Transactional(readOnly = true)
    public QuestionResponse getQuestionById(UUID quizId, UUID questionId) {
        if (!quizRepository.existsById(quizId)) {
            throw new EntityNotFoundException("Quiz not found: " + quizId);
        }

        return questionRepository.findByIdAndQuiz_Id(questionId, quizId)
                .map(questionMapper::toDto)
                .orElseThrow(() ->  new EntityNotFoundException("Question not found: " + questionId));
    }

    public QuestionResponse updateQuestion(UUID quizId, UUID questionId, UpdateQuestionRequest request) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found: " + quizId));

        if (quiz.getStatus() != QuizStatus.DRAFT) {
            throw new IllegalStateException("Questions can only be updated in draft quizzes.");
        }

        Question question = questionRepository.findByIdAndQuiz_Id(questionId, quizId).orElseThrow(() -> new EntityNotFoundException("Question not found: " + questionId));

        validateAnswerOptions(request.answerOptions());

        if (question.getQuestionOrder() != request.questionOrder() && questionRepository.existsByQuiz_IdAndQuestionOrder(quizId, request.questionOrder())) {
            throw new IllegalStateException("A question with this order already exists in the quiz.");
        }

        question.setQuestionText(request.questionText());
        question.setQuestionOrder(request.questionOrder());
        question.setTimeLimitSeconds(request.timeLimitSeconds());

        for (AnswerOption option : List.copyOf(question.getAnswerOptions())) {
            question.removeAnswerOption(option);
        }

        for (var optionRequest : request.answerOptions()) {
            AnswerOption option = AnswerOption.builder()
                    .answerText(optionRequest.answerText())
                    .optionOrder(optionRequest.optionOrder())
                    .isCorrect(optionRequest.isCorrect())
                    .build();

            question.addAnswerOption(option);
        }

        Question updatedQuestion = questionRepository.save(question);

        return questionMapper.toDto(updatedQuestion);
    }

    public void deleteQuestionById(UUID quizId, UUID questionId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found: " + quizId));

        if (quiz.getStatus() != QuizStatus.DRAFT) {
            throw new IllegalStateException("Questions can only be deleted in draft quizzes.");
        }

        Question question = questionRepository.findByIdAndQuiz_Id(questionId, quizId).orElseThrow(() -> new EntityNotFoundException("Question not found: " + questionId));
        questionRepository.delete(question);
    }


    private void validateAnswerOptions(List<? extends AnswerOptionRequest> answerOptions) {
        if (answerOptions.size() < 2 || answerOptions.size() > 4) {
            throw new IllegalStateException("A question must have between 2 and 4 answer options.");
        }

        long correctAnswers = answerOptions.stream()
                .filter(AnswerOptionRequest::isCorrect)
                .count();

        if (correctAnswers != 1) {
            throw new IllegalStateException("A question must have exactly one correct answer.");
        }

        long distinctOrders = answerOptions.stream()
                .map(AnswerOptionRequest::optionOrder)
                .distinct()
                .count();

        if (distinctOrders != answerOptions.size()) {
            throw new IllegalStateException("Answer option orders must be unique.");
        }
    }
}

