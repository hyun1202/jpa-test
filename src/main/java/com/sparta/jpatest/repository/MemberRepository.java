package com.sparta.jpatest.repository;

import com.sparta.jpatest.entity.Member;
import com.sparta.jpatest.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
