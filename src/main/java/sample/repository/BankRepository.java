package sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.domain.Bank;

public interface BankRepository extends JpaRepository<Bank, Long> {

    Bank findByName(String name);

}
