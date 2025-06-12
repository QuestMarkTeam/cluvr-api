package com.example.cluvrapi.domain.clubMember.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cluvrapi.domain.clubMember.entity.ClubMember;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long>, ClubMemberRepositoryCustom {
}
