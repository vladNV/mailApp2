package sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.domain.Signature;

public interface SignatureRepository extends JpaRepository<Signature, Long> {
}
