package com.sparta.jpatest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String name, Team team) {
        this.name = name;
        this.team = team;
//        // 연관관계 편의 메서드
//        team.addMember(this);
    }

    public void setTeam2(Team team){
        this.team = team;
        team.getMembers().add(this);
    }

    public void setTeam3_problem(Team team){
        this.team = team;
//        team.getMembers().add(this);
    }

    public void setTeam3(Team team){
        if (this.team != null) {	// this.team이 null이 아니면 이 member객체는 team이 있음을 의미
            this.team.getMembers().remove(this);		// 해당 팀의 멤버에서 삭제
        }
        this.team = team;
        team.getMembers().add(this);
    }

    public Member() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Team getTeam() {
        return team;
    }

    public void setName(String name) {
        this.name = name;
    }
}