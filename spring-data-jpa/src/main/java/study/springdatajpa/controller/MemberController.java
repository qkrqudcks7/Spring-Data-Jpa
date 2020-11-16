package study.springdatajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.springdatajpa.dto.MemberDto;
import study.springdatajpa.entity.Member;
import study.springdatajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/member/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }
    // JPA DATA : 도메인 클래스 컨버터
    // pk일때 사용 but 잘 쓰진 않음
    @GetMapping("/member2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    // JPA DATA Page
    // page 개수 설정하는법
        // 1. application.yml에서 global 설정을 함
        // 2. 글로벌말고 얘 한테만 하고싶다? controller에서 직접 설정함(size , sort 다양하게 가능)
    @GetMapping("/member")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable){
        // 엔티를 그대로 노출하면 안됨 dto로 변환 시켜라.
        Page<Member> page = memberRepository.findAll(pageable);
        Page<MemberDto> map = page.map(member -> new MemberDto(member));
        return map;
    }

    @PostConstruct
    public void init() {
        for (int i=0;i<100;i++){
            memberRepository.save(new Member("user"+i,i));
        }

    }
}
