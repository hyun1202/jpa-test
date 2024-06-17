package com.sparta.jpatest;

import com.sparta.jpatest.entity.Member;
import com.sparta.jpatest.entity.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("다대일 관계에서 주로 발생하는 에러")
public class MyDataJPA1ErrorTest extends MyDataJpaTest {

    @DisplayName("1. 다대일 관계에서 주로 발생 하는 에러 - 예제1")
    @Test
    void test1_problem() {
        Team team = new Team("team1");
        // 영속성 부여
        entityManager.persist(team);

        Member member = new Member("member1", team);
        entityManager.persist(member);

        System.out.println("=================================");
        // Team에 Member를 지정해주지 않았으므로 값이 없음
        List<Member> findMembers = team.getMembers();
        // 값이 없으므로 아래 반복문은 실행되지 않는다.
        for (Member findMember : findMembers) {
            System.out.println(findMember.getName()); // 1번
        }
        System.out.println("=================================");

        // 아직 위의 내용이 데이터베이스에 반영이 되지 않았으므로 영속성 컨텍스트에서 값을 꺼내온다.
        Team findTeam = entityManager.find(Team.class, team.getId());
        List<Member> teamMembers = findTeam.getMembers();
        // members엔 아무것도 없으므로 IndexOutOfBoundsException 발생
        Assertions.assertThrows(IndexOutOfBoundsException.class,
                () -> teamMembers.get(0).setName("member2"));

        // member1 출력
        System.out.println(member.getName());
    }

    @DisplayName("2. 다대일 관계에서 주로 발생 하는 에러 - 예제1 해결")
    @Test
    void test1_solve() {
        Team team = new Team("team1");
        // 영속성 부여
        entityManager.persist(team);

        Member member = new Member("member10", team);
        // 연관관계 변경 반영
        team.addMember(member);
        entityManager.persist(member);

        System.out.println("=================================");
        List<Member> findMembers = team.getMembers();
        for (Member findMember : findMembers) {
            System.out.println(findMember.getName()); // 1번
        }
        System.out.println("=================================");

        // 영속 객체가 있으므로 1차 캐시에서 값을 찾는다 (select query 발생 x)
        Team findTeam = entityManager.find(Team.class, team.getId());
        List<Member> teamMembers = findTeam.getMembers();
        teamMembers.get(0).setName("member2");

        // 영속 객체가 변경되었으므로 변경된 member2를 출력
        System.out.println(member.getName());

        // 메서드 종료 이후 transaction.commit() 발생.
    }

    @DisplayName("3. 다대일 관계에서 주로 발생 하는 에러 재현 및 해결 - 예제2")
    @Test
    void test2_problem_solve() {
        Team team = new Team("team1");
        // 영속성 부여
        entityManager.persist(team);

        Member member = new Member("member1", team);
        entityManager.persist(member);

        // flush 작업
        entityManager.flush();

        // 영속성 컨텍스트를 비워준다.
        entityManager.clear();

        // 영속성 컨텍스트가 비워진 이후 member명 변경
        // JPA에서는 알 수 없다.
        member.setName("member2");
        // 1차 캐시에 값이 존재하지 않으므로 db로 select를 날려 찾는다.
        Member findMember = entityManager.find(Member.class, member.getId());

        // 변경 전 이름이 나온다.
        System.out.println(findMember.getName());
        Assertions.assertNotEquals("member2", findMember.getName());

        entityManager.merge(member);

        // 영속성 컨텍스트에 해당하는 값이 있으므로, 1차 캐시에서 값을 가져온다. (조회 쿼리 미발생)
        findMember = entityManager.find(Member.class, member.getId());

        // 변경 후 이름이 나온다.
        System.out.println(findMember.getName());
        Assertions.assertEquals("member2", findMember.getName());
    }
}
