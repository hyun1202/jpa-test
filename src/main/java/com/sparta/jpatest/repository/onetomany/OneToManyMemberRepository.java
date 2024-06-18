package com.sparta.jpatest.repository.onetomany;

import com.sparta.jpatest.entity.onetomany.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OneToManyMemberRepository extends JpaRepository<Member, Long> {
}
