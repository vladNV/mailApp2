package sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.domain.Box;

public interface BoxRepository extends JpaRepository<Box, Long> {
}
