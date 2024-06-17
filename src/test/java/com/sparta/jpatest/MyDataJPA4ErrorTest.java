package com.sparta.jpatest;

import com.sparta.jpatest.entity.Member;
import com.sparta.jpatest.entity.Team;
import com.sparta.jpatest.repository.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DisplayName("4. 연관관계 조회시 발생하는 N+1 에러")
public class MyDataJPA4ErrorTest extends MyDataJpaTest {

    @Autowired
    TeamRepository teamRepository;

    @DisplayName("즉시로딩 및 지연로딩 N+1 문제")
    @Test
    void test1_problem() {
        // @OneToMany에 fetch = FetchType.EAGER 지정 시 N+1문제 발생
        // 지연로딩의 경우 발생하지 않음
        List<Team> teams = teamRepository.findAll();

        // 지연로딩의 경우 해당 객체가 사용될 때 쿼리 발생
        System.out.println("============== N+1 시점 확인용 ===================");
        teams.stream().forEach(team -> {
            team.getMembers().size();
        });

        // 결국 즉시로딩이나 지연로딩은 N+1 문제가 발생되는 시점만 다를 뿐 막상 객체를 탐색할 때에 N+1 문제가 발생
    }

    @DisplayName("해결1. FetchJoin 사용")
    @Test
    void test1_solve1() {
        // fetchJoin 메소드 생성
        // 지연로딩이나 즉시로딩 상관 없이 쿼리가 하나만 날라간다
        List<Team> teams = teamRepository.findAllFetchJoin();

        System.out.println("============== N+1 시점 확인용 ===================");
        teams.stream().forEach(team -> {
            team.getMembers().size();
        });
    }

    @DisplayName("해결2. BatchSize 사용")
    @Test
    void test1_solve2() {
        // application.yml에 default_batch_fetch_size 지정
        // 지연로딩이나 즉시로딩 상관 없이 쿼리가 하나만 날라가나 in절로 쿼리가 나감
        List<Team> teams = teamRepository.findAll();

        System.out.println("============== N+1 시점 확인용 ===================");
        teams.stream().forEach(team -> {
            team.getMembers().size();
        });
    }
}
