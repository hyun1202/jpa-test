package com.sparta.jpatest;

import com.sparta.jpatest.entity.onetomany.Member;
import com.sparta.jpatest.entity.onetomany.Team;
import com.sparta.jpatest.repository.MemberRepository;
import com.sparta.jpatest.repository.TeamRepository;
import com.sparta.jpatest.repository.onetomany.OneToManyMemberRepository;
import com.sparta.jpatest.repository.onetomany.OneToManyTeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.PrintStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("6. OneToMany 단방향 매핑 이슈")
public class MyDataJPA6ErrorTest extends MyDataJpaTest {

    @Autowired
    OneToManyTeamRepository oneToManyTeamRepository;

    @Autowired
    OneToManyMemberRepository oneToManyMemberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("객체 저장 시 update 쿼리문 추가 발생 - 팀에 멤버 추가")
    @Test
    void test1_problem() {
        // Team-Member의 관계는 oneToMany
        Member oneToManyMember = Member.builder()
                .name("oneToManyMember1")
                .build();

        Member oneToManyMember2 = Member.builder()
                .name("oneToManyMember2")
                .build();

        oneToManyMemberRepository.save(oneToManyMember);
        oneToManyMemberRepository.save(oneToManyMember2);

        Team team = new Team("team");

        // 1. 두 번의 update 쿼리 발생
        // update one_to_many_member set team_id=1 where id=1
        // update one_to_many_member set team_id=1 where id=2
        team.addMember(oneToManyMember);
        team.addMember(oneToManyMember2);

        oneToManyTeamRepository.save(team);

        entityManager.flush();
    }

    @DisplayName("객체 삭제 시 update 쿼리문 추가 발생 - 멤버 삭제")
    @Test
    void test2_problem() {
        /////////////////////////////////////////
        // Team-Member의 관계는 oneToMany
        Member oneToManyMember = Member.builder()
                .name("oneToManyMember1")
                .build();

        Member oneToManyMember2 = Member.builder()
                .name("oneToManyMember2")
                .build();

        oneToManyMemberRepository.save(oneToManyMember);
        oneToManyMemberRepository.save(oneToManyMember2);

        Team team = new Team("team");

        // 두 번의 update 쿼리 발생
        team.addMember(oneToManyMember);
        team.addMember(oneToManyMember2);

        Team saveTeam = oneToManyTeamRepository.save(team);

        entityManager.flush();
        ////////////////////////////////////////

        System.out.println("테스트 시작");

        Team saved = oneToManyTeamRepository.findById(1L).get();
        List<Member> members = saved.getMembers();
        // 1. Team에 있는 멤버 리스트에서 멤버 삭제
        members.removeIf(member -> member.getId().equals(1L));
        // 2. id가 1인 멤버 삭제
        oneToManyMemberRepository.deleteById(1L);

        // update문과 delete문이 발생
        // 1. update: members에서 member를 제거 했으므로 member에 team_id를 null로 만드는 쿼리 발생
        //     update one_to_many_member set team_id=null
        //     where team_id=1 and id=1
        // 2. delete: deleteById()를 했으니, delete 쿼리 발생
        entityManager.flush();
        entityManager.clear();

        // member가 삭제되었는지
        oneToManyMemberRepository.findById(1L).ifPresent(member -> {
            throw new IllegalArgumentException("삭제되지 않았습니다");
        });

        // team에서 member가 삭제되었는지
        Team findTeam = oneToManyTeamRepository.findById(1L).orElse(null);
        assertThat(findTeam.getMembers()).hasSize(1);
    }

    @DisplayName("객체 저장 시 update 쿼리문 추가 발생 - 해결 실패 (ManyToOne 양방향)")
    @Test
    void test1_solve() {
        com.sparta.jpatest.entity
                .Member member = com.sparta.jpatest.entity.Member
                    .builder()
                    .name("solveMember")
                    .build();

        com.sparta.jpatest.entity.Member member2 = com.sparta.jpatest.entity.Member.builder()
                .name("solveMember2")
                .build();

        memberRepository.save(member);
        memberRepository.save(member2);

        com.sparta.jpatest.entity.
                Team team = new com.sparta.jpatest.entity.Team("team");

        // 하나의 update 쿼리가 나가지 않음..
        team.addMember_solve6(member);
        team.addMember_solve6(member2);
//        member.setTeam3_problem(team);
//        member2.setTeam3_problem(team);

        com.sparta.jpatest.entity.
                Team saveTeam = teamRepository.save(team);

        entityManager.flush();
        entityManager.clear();

        com.sparta.jpatest.entity.
                Team findTeam = teamRepository.findById(saveTeam.getId()).orElse(null);

        findTeam.getMembers().stream()
                .map(m -> m.getName())
                .forEach(System.out::println);
    }

    @DisplayName("객체 삭제 시 update 쿼리문 추가 발생 - 해결 (ManyToOne 양방향)")
    @Test
    void test2_solve() {
        System.out.println("테스트 시작");

        com.sparta.jpatest.entity.
                Team saved = teamRepository.findById(1L).get();

        List<com.sparta.jpatest.entity.Member> members = saved.getMembers();
        // 1. Team에 있는 멤버 리스트에서 멤버 삭제
        members.removeIf(m -> m.getId().equals(1L));
        // 2. id가 1인 멤버 삭제
        // 영속성 전이 테스트
//        memberRepository.deleteById(1L);

        // 한번의 delete 쿼리 발생
        // Team에 @OneToMany(mappedBy = "team", orphanRemoval = true, cascade = CascadeType.ALL)로 영속성 전이 시, delete 메소드를 사용하지 않아도 자동으로 삭제해준다
        entityManager.flush();
        entityManager.clear();

        // member가 삭제되었는지
        memberRepository.findById(1L).ifPresent(m -> {
            throw new IllegalArgumentException("삭제되지 않았습니다");
        });

        // team에서 member가 삭제되었는지
        com.sparta.jpatest.entity.Team findTeam = teamRepository.findById(1L).orElse(null);
        assertThat(findTeam.getMembers()).hasSize(1);
    }
}
