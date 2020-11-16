package study.springdatajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import study.springdatajpa.dto.MemberDto;
import study.springdatajpa.entity.Member;
import study.springdatajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class JpaDataMemberTest {
    @Autowired
    JpaDataMember jpaDataMember;

    @PersistenceContext
    EntityManager em;

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        jpaDataMember.save(member1);
        jpaDataMember.save(member2);

        Member findMember1 = jpaDataMember.findById(member1.getId()).get();
        Member findMember2 = jpaDataMember.findById(member2.getId()).get();
        assertEquals(findMember1,member1);
        assertEquals(findMember2,member2);

        List<Member> members = jpaDataMember.findAll();
        assertEquals(members.size(),2);

        long count = jpaDataMember.count();
        assertEquals(count,2);

        jpaDataMember.delete(member1);
        jpaDataMember.delete(member2);

        long deleteCount = jpaDataMember.count();
        assertEquals(deleteCount,0);
    }

    @Test
    public void findByUsernaeAndAge(){
        Member member1 = new Member("aaa",30);
        Member member2 = new Member("member2",30);
        jpaDataMember.save(member1);
        jpaDataMember.save(member2);

        List<Member> result= jpaDataMember.findByUsernameAndAgeGreaterThan("aaa",15);

        assertEquals(result.get(0).getUsername(),"aaa");
        assertEquals(result.get(0).getAge(),30);
        assertEquals(result.size(),1);
    }

    @Test
    public void testQuery(){
        Member member1 = new Member("aaa",30);
        Member member2 = new Member("member2",30);
        jpaDataMember.save(member1);
        jpaDataMember.save(member2);

        List<Member> result = jpaDataMember.findUser("aaa", 20);
        assertEquals(result.get(0),member1);

    }
    @Test
    public void returnType(){
        Member member1 = new Member("aaa",30);
        Member member2 = new Member("member2",30);
        jpaDataMember.save(member1);
        jpaDataMember.save(member2);

        Member aaa = jpaDataMember.find3ByUsername("aaa");
        System.out.println("findMember="+aaa);

    }

    @Test
    public void pageing() {
        jpaDataMember.save(new Member("member1",10));
        jpaDataMember.save(new Member("member2",10));
        jpaDataMember.save(new Member("member3",10));
        jpaDataMember.save(new Member("member4",10));
        jpaDataMember.save(new Member("member5",10));

        int age=10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = jpaDataMember.findByAge(age, pageRequest);
        // 엔티티를 그대로 노출시키면 안된다 dto로 변환시킴
        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("member="+member);
        }
        System.out.println("totalElements="+totalElements);
    }

    @Test
    public void bulkUpdate() {
        jpaDataMember.save(new Member("member1", 10));
        jpaDataMember.save(new Member("member2", 10));
        jpaDataMember.save(new Member("member3", 20));
        jpaDataMember.save(new Member("member4", 21));
        jpaDataMember.save(new Member("member5", 40));

        int resultCount= jpaDataMember.bulkAgePlus(20);
        // 벌크연산에는 이걸 해줘라 꼭
        em.flush();
        em.clear();

        List<Member> result = jpaDataMember.findByUsername("member5");

        Member member= result.get(0);
        System.out.println("member5="+member);

        assertEquals(resultCount,3);
    }

}