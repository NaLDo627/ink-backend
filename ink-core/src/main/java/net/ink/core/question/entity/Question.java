package net.ink.core.question.entity;

import lombok.*;
import net.ink.core.member.entity.Member;
import net.ink.core.reply.entity.Reply;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member author;

    @Column(length = 20)
    private String category;

    @Column(nullable = false, length = 150)
    private String content;

    @Column(name = "ko_content")
    private String koContent;

    @Builder.Default
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WordHint> wordHints = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    @Builder.Default
    @Column(name = "reg_date", nullable = false)
    private LocalDateTime regDate = LocalDateTime.now();

    public void removeReply(Reply reply) {
        this.replies = this.replies.stream()
                .filter(x -> !Objects.equals(x.getReplyId(), reply.getReplyId())).collect(Collectors.toList());
    }
}
