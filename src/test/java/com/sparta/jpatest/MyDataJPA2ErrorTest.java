package com.sparta.jpatest;

import com.sparta.jpatest.entity.Member;
import com.sparta.jpatest.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("2. 연관 객체의 변경사항이 저장되지 않는 에러")
public class MyDataJPA2ErrorTest extends MyDataJpaTest {

    @DisplayName("양방향 연관관계 저장 - 에러 재현")
    @Test
    void test1_problem() {
        // 양방향 관계 설정 시 mappedBy 설정 필요. (연관관계 주인 필드 설정)
        Long team1_id = 1L;

        Team team = entityManager.find(Team.class, team1_id);

        Member member1 = Member.builder()
                .id(3L)
                .name("member10")
                .build();

        Member member2 = Member.builder()
                .id(2L)
                .name("member11")
                .build();

        // 저장되지 않는다.
        team.getMembers().add(member1);
        team.getMembers().add(member2);


        entityManager.flush();
        // 반영 사항 확인을 위함
        entityManager.clear();

        Team findTeam = entityManager.find(Team.class, team1_id);

        // member1, member5
        findTeam.getMembers().stream()
                .forEach(member -> System.out.println(member.getName()));
    }

    @DisplayName("양방향 연관관계 저장 - 해결")
    @Test
    void test1_solve() {
        // 양방향 관계 설정 시 mappedBy 설정 필요. (연관관계 주인 필드 설정)
        Long team1_id = 1L;

        Team team = entityManager.find(Team.class, team1_id);

        Member member1 = Member.builder()
                .name("member10")
                .build();

        Member member2 = Member.builder()
                .name("member11")
                .build();

        // 연관 관계의 주인에 해당 객체를 넣어준다.
        member1.setTeam2(team);
        member2.setTeam2(team);

        // db 저장을 위해 연관 관계의 주인 영속성 부여
        entityManager.persist(member1);
        entityManager.persist(member2);

        // insert 쿼리 전송
        entityManager.flush();

        // 반영 사항 확인을 위함
        entityManager.clear();

        // 변경사항 확인
        Team findTeam = entityManager.find(Team.class, team1_id);

        // member1, member5, member10, member11
        findTeam.getMembers().stream()
                .forEach(member -> System.out.println(member.getName()));
    }
}
