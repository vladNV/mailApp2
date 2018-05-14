package sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.domain.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
