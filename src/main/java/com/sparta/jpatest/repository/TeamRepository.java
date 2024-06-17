package com.sparta.jpatest.repository;

import com.sparta.jpatest.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    @Query("select t from Team t join fetch t.members")
    List<Team> findAllFetchJoin();
}
