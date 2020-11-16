package study.springdatajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.springdatajpa.dto.MemberDto;
import study.springdatajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long>,MemberRepositoryCutom {

    List<Member> findByUsernameAndAgeGreaterThan(String username , int age);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username , @Param("age") int age);

    @Query("select new study.springdatajpa.dto.MemberDto(m.id,m.username,t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    // 컬렉션
    List<Member> findListByUsername(String username);
    // 단건
    Member findMemberByUsername(String username);
    // 단건 optional
    Optional<Member> findOptionalByUsername(String username);

    // JPA DATA 페이징
    // 쿼리문을 작성해서 카운트쿼리룰 꼭 분리해라 , 안 그럼 성능 ㅈㄴ 떨어진다
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true) // 이게 있어야 executeUpdate를 실행함
    @Query("update Member m set m.age=m.age+1 where m.age>=:age")
    int bulkAgePlus(@Param("age") int age);

    // 엔티티 그래프 :  fetch join을 적용하려면 @Query로 쿼리문을 다 써야함, but
    //               findById처럼 정해진 문구에도 쓰려면 @Query를 적어야 된다.
    //               이런 귀찮음을 줄여주기 위해 엔티티 그래프를 씀
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    // 딱히 쓸일 없음 ,최적화용
   @QueryHints(value = @QueryHint(name="org.hibernate.readOnly",value = "true"))
    Member findReadOnlyByUsername(String username);

   // 내용이 넘 깊어서 이런게 있다.
   @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
