package com.sparta.jpatest;

import com.sparta.jpatest.entity.Member;
import com.sparta.jpatest.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("3. 연관 객체의 변경사항이 업데이트 되지 않는 에러")
public class MyDataJPA3ErrorTest extends MyDataJpaTest {

    @DisplayName("에러 재현")
    @Test
    void test1_problem() {
        Long team1_id = 1L;
        Long team2_id = 2L;

        Long member1_id = 1L;

        Team team2 = entityManager.find(Team.class, team2_id);

        // member1의 소속 팀은 현재 1
        Member member1 = entityManager.find(Member.class, member1_id);

        // member1의 소속 팀이 2로 변경됨
        // 해당 메소드는 member에 team만 지정해준다 (this.team = team)
        member1.setTeam3_problem(team2);

        for (Member member : team2.getMembers()) {
            // 순수 자바 객체이므로 아직 변경된 값이 변하지 않았다.
            // member1이 team2의 회원 목록에 없어 출력되지 않는다.
            System.out.println(member.getName());
        }
    }

    @DisplayName("해결")
    @Test
    void test1_solve() {
        Long team2_id = 2L;

        Long member1_id = 1L;

        Team team2 = entityManager.find(Team.class, team2_id);

        // member1의 소속 팀은 현재 1
        Member member1 = entityManager.find(Member.class, member1_id);

        // member1의 소속 팀이 2로 변경됨
        // 해당 메소드는 member에 team만 지정하고 연관관계인 team 객체의 members 필드에도 변경해준다.
        member1.setTeam3(team2);

        for (Member member : team2.getMembers()) {
            // 순수 자바객체이지만, 해당 객체에 연관 관계인 객체에도 변경을 해주었으므로 member1이 출력
            System.out.println(member.getName());
        }
    }
}
