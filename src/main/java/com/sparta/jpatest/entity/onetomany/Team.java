package com.sparta.jpatest.entity.onetomany;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "one_to_many_team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    // 1:N 단방향
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    // member 테이블에 외래키 생성, 외래키 관리는 Team에서 함
    @JoinColumn(name = "team_id")
    private List<Member> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }

    public void addMember(Member member) {
        this.members.add(member);
    }
}