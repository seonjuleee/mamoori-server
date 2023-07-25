package com.mamoori.mamooriback.api.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "id")
    private Long authTokenId;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "social_type")
    private String socialType;

//    @Column(name = "expired")
//    private long expired;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
