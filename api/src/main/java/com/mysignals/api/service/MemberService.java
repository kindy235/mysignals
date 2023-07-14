package com.mysignals.api.service;

import java.util.List;
import java.util.Optional;
import com.mysignals.api.entity.Member;

public interface MemberService {

    Member saveMember(Member member);

    List<Member> getAllMembers();

    Optional<Member> getMemberById(Long id);

    void deleteMemberById(Long id);

    public boolean existsById(Long id);
}
