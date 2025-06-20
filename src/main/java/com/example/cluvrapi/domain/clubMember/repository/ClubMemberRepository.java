package com.example.cluvrapi.domain.clubMember.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.clubMember.entity.ClubMember;

@Repository
public interface ClubMemberRepository extends JpaRepository<ClubMember, Long>, ClubMemberRepositoryCustom {
}
