package com.mamoori.mamooriback.api.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "user_checklist")
public class UserChecklist extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_checklist_id")
    private Long userChecklistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "userChecklist", orphanRemoval = true)
    private List<UserChecklistAnswer> userChecklistAnswers = new ArrayList<>();

    public UserChecklist(User user) {
        this.user = user;
    }
}
