package dk.martinrohwedder.quiz_arena_backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "questions")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "question_text", length = 500, nullable = false)
    private String questionText;

    @Column(name = "question_order", nullable = false)
    private Integer questionOrder;

    @Builder.Default
    @Column(name = "time_limit_seconds", nullable = false)
    private Integer timeLimitSeconds = 20;

    @Builder.Default
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerOption> answerOptions = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /*************************************************************
     * Help methods for consistent relationships
     ************************************************************/

    public void addAnswerOption(AnswerOption answerOption) {
        answerOptions.add(answerOption);
        answerOption.setQuestion(this);
    }

    public void removeAnswerOption(AnswerOption answerOption) {
        if (answerOptions.remove(answerOption)) {
            answerOption.setQuestion(null);
        }
    }
}
