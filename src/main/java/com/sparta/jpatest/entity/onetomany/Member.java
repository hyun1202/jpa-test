package com.sparta.jpatest.entity.onetomany;

import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity(name = "one_to_many_member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "team_id", insertable = false, updatable = false)
    private Long teamId;

    @Builder
    public Member(String name) {
        this.name = name;
    }
}