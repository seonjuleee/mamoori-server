package com.mamoori.mamooriback.api.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Checklist extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checklist_id")
    private Long checklistId;

    @Column
    private String description;

    @Column(name = "order_num")
    private Integer order;

    @OneToMany(mappedBy = "checklist")
    private List<UserChecklistAnswer> userChecklists = new ArrayList<>();
}
