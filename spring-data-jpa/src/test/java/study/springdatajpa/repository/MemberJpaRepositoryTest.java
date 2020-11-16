package study.springdatajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.springdatajpa.entity.Member;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member= new Member("박병찬");
        Member save = memberJpaRepository.save(member);

        Member find = memberJpaRepository.find(save.getId());

        assertEquals(find.getId(),member.getId());
        assertEquals(find.getUsername(),member.getUsername());
        assertEquals(find,member);
    }

    @Test
    public void basicCRUD(){
        Member member1= new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        Member findeMember1 = memberJpaRepository.findNyId(member1.getId()).get();
        Member findeMember2 = memberJpaRepository.findNyId(member2.getId()).get();

        assertEquals(findeMember1,member1);
        assertEquals(findeMember2,member2);
    }
    @Test
    public void findByUserNameAndAge(){
        Member m1 = new Member("aaa",10);
        Member m2 = new Member("aaa",20);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUserNameAndAge("aaa", 15);

        assertEquals(result.get(0).getUsername(),"aaa");
        assertEquals(result.get(0).getAge(),20);
        assertEquals(result.size(),1);
    }

    @Test
    public void bulkUpdate(){
        memberJpaRepository.save(new Member("member1",10));
        memberJpaRepository.save(new Member("member2",19));
        memberJpaRepository.save(new Member("member3",20));
        memberJpaRepository.save(new Member("member4",21));
        memberJpaRepository.save(new Member("member5",40));

        int resultCount=memberJpaRepository.bulkAgePlus(20);
        assertEquals(resultCount,3);
    }
}