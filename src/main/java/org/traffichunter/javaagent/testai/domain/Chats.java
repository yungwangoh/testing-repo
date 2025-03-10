package org.traffichunter.javaagent.testai.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Chats {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question")
    private String question;

    @Column(name = "answer")
    private String answer;

    @JoinColumn(name = "thread_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Threads thread;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deleted")
    private boolean deleted;

    @Builder
    public Chats(final String question,
                 final String answer,
                 final Threads thread,
                 final LocalDateTime createdAt,
                 final boolean deleted) {

        this.question = question;
        this.answer = answer;
        this.thread = thread;
        this.createdAt = createdAt;
        this.deleted = deleted;
    }

    public void deleteChat() {
        this.deleted = true;
    }
}
