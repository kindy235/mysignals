package com.mysignals.api.service.impl;

import com.mysignals.api.entity.Member;
import com.mysignals.api.repository.MemberRepository;
import com.mysignals.api.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public void deleteMemberById(Long id) {
        memberRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return memberRepository.existsById(id);
    }

}
