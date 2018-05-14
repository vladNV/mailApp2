package sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.domain.Mail;

public interface MailRepository extends JpaRepository<Mail, Long> {
}
