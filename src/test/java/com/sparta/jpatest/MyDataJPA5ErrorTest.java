package com.sparta.jpatest;

import com.sparta.jpatest.entity.Team;
import com.sparta.jpatest.repository.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DisplayName("5. save() 이후 변경사항이 반영되지 않는 에러")
public class MyDataJPA5ErrorTest extends MyDataJpaTest {

    @Autowired
    TeamRepository teamRepository;

    @DisplayName("에러 재현")
    @Test
    void test1_problem() {
        // 영속 객체가 아니다.
        Team team = Team.builder()
                        .id(100L)   // id값이 있으면 준영속, 없으면 비영속 상태로 본다.
                        .name("teamName")
                        .build();
        // save 이후 영속성이 부여된 객체를 반환
        Team saveTeam = teamRepository.save(team); //id 값이 있으므로 merge()를 호출

        // team은 영속 객체가 아니므로 flush()를 해도 쿼리가 발생하지 않음.
        team.setName("updateTeamName");

        // 이후 flush()를 해도 쿼리가 발생하지 않는다.
        entityManager.flush();
        entityManager.clear();

        // 1차 캐시에 아무것도 없으므로 select문 발생
        Team findTeam = entityManager.find(Team.class, saveTeam.getId());

        // teamName 출력
        System.out.println(findTeam.getName());
    }

    @DisplayName("해결")
    @Test
    void test1_solve() {
        Team team = Team.builder()
//                .id(100L)   // id값이 있든 없든 상관은 없다.
                .name("teamName")
                .build();

        Team saveTeam = teamRepository.save(team);  //persist() 호출

        // 영속 객체를 변경했으므로, flush()를 하니 update문 쿼리가 발생
        saveTeam.setName("updateTeamName");

        entityManager.flush();
        entityManager.clear();

        // 1차 캐시에 아무것도 없으므로 select문 발생
        Team findTeam = entityManager.find(Team.class, saveTeam.getId());

        // updateTeamName 출력
        System.out.println(findTeam.getName());
    }
}
