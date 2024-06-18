package com.sparta.jpatest;

import com.sparta.jpatest.entity.Member;
import com.sparta.jpatest.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DataJpaTest
class MyDataJpaTest {

    @PersistenceContext
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 설정
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");
        Team team3 = new Team("team3");

        Member member1 = new Member("member1", team1);
        Member member2 = new Member("member2", team2);
        Member member3 = new Member("member3", team3);
        Member member4 = new Member("member4", team2);
        Member member5 = new Member("member5", team1);

        entityManager.persist(team1);
        entityManager.persist(team2);
        entityManager.persist(team3);
        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);
        entityManager.persist(member5);

        entityManager.clear();
        System.out.println("============== 데이터 삽입 완료 ==============");
    }
}
