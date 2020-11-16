package study.springdatajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.springdatajpa.dto.MemberDto;
import study.springdatajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface JpaDataMember extends JpaRepository<Member,Long> {
    List<Member> findByUsername(String username);

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.springdatajpa.dto.MemberDto(m.id , m.username , t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> find2ByUsername(String username); // 컬렉션 없는 값일때 : 빈 값을 반환함 0을 반환 null아님 주의
    Member find3ByUsername(String username); // 단건 없는 값일때 : null을 반환함
    Optional<Member> finde3ByUsername(String username);

    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying // 꼭 넣어야 됨
    @Query("update Member m set m.age=m.age+1 where m.age >=:age")
    int bulkAgePlus(@Param("age") int age);

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();
}
