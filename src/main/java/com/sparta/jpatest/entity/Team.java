package com.sparta.jpatest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // N+1 테스트를 위한 즉시로딩 지정
//    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER)
//    @OneToMany(mappedBy = "team")
    // 영속성 전이 테스트
    @OneToMany(mappedBy = "team", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Member> members = new ArrayList<>();

    public Team() {
    }

    public Team(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void addMember(Member member) {
        members.add(member);
    }

    public void addMember_solve6(Member member) {
        members.add(member);
        member.setTeam3_problem(this);
    }

    public void setName(String name) {
        this.name = name;
    }
}