package study.springdatajpa.repository;

import lombok.RequiredArgsConstructor;
import study.springdatajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;
@RequiredArgsConstructor
// MemberRepository(앞의 멤버리포지토리 똑같아야된다) + Impl(꼭 붙여야 함)
public class MemberRepositoryImpl implements MemberRepositoryCutom{

    private final EntityManager em;

    @Override
    public List<Member> findeMemberCutom() {
        return em.createQuery("select m from Member m").getResultList();
    }
}
