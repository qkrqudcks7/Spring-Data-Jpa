package study.springdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.springdatajpa.entity.Team;

public interface JpaDataTeam extends JpaRepository<Team,Long> {
}
