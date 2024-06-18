package com.sparta.jpatest.repository.onetomany;

import com.sparta.jpatest.entity.onetomany.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OneToManyTeamRepository extends JpaRepository<Team, Long> {
}
