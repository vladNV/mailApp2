package sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.domain.MailPattern;

public interface MailPatternRepository extends JpaRepository<MailPattern, Long> {
}
