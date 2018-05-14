package sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sample.domain.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query(value = "select * from client c where c.login like ?1",
    nativeQuery = true)
    Client findByLogin(String username);


}
