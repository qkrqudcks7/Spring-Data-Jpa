package study.springdatajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.springdatajpa.dto.MemberDto;
import study.springdatajpa.entity.Member;
import study.springdatajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("박병찬");
        Member save = memberRepository.save(member);

        Member find = memberRepository.findById(save.getId()).get();

        assertEquals(find.getId(), member.getId());
        assertEquals(find.getUsername(), member.getUsername());
        assertEquals(find, member);
    }

    @Test
    public void findByUserNameAndAge(){
        Member m1 = new Member("aaa",10);
        Member m2 = new Member("aaa",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("aaa", 15);

        assertEquals(result.get(0).getUsername(),"aaa");
        assertEquals(result.get(0).getAge(),20);
        assertEquals(result.size(),1);
    }

    @Test
    public void findMemberDto(){
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("aaa",10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for(MemberDto dto : memberDto) {
            System.out.println("dto=" + dto);
        }

    }
    @Test
    public void findByNames(){
        Member m1 = new Member("aaa",10);
        Member m2 = new Member("bbb",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("aaa" , "bbb"));
        for(Member member : result) {
            System.out.println(member);
        }
    }

    @Test
    public void pageing() {
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        int age=10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "username");

        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // 엔티티가 아니라 dto로 변환해서 api로 써도 된다.
        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("member="+member);
        }
        System.out.println("totalElements="+totalElements);
    }

    @Test
    public void bulkUpdate(){
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",19));
        memberRepository.save(new Member("member3",20));
        memberRepository.save(new Member("member4",21));
        memberRepository.save(new Member("member5",40));

        // bulk연산 주의 : 영속성 컨테스트를 무시하고 결과 값을 반환하기 때문에 영속성 컨테스트에 알려야 됨
       int resultCount=memberRepository.bulkAgePlus(20);
      // em.flush();
      // em.clear();
       // 이렇게 되면 +1된 값으로 영속성 컨테스트로 반영이 됨. 이렇게 해도 돼고 Modifying에 넣어도 됨

        assertEquals(resultCount,3);
    }

    @Test
    public void queryHint() {
        Member member1 = new Member("member1",10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    public void lock() {
        Member member1 = new Member("member1",10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        List<Member> result = memberRepository.findLockByUsername("member1");
    }

    @Test
    public void callCustom(){
        List<Member> result = memberRepository.findeMemberCutom();
    }
}