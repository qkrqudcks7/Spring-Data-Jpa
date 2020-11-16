package study.springdatajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist // persist하기전에 이벤트 발생
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        // 데이터를 now로 초기화 시켜 놓음
        createdDate = now;
        updatedDate = now;
    }

    // update가 있을때 갱신시켜준다
    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
