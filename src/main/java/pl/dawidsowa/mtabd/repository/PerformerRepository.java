package pl.dawidsowa.mtabd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dawidsowa.mtabd.domain.Performer;


/**
 * Spring Data JPA repository for the Performer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PerformerRepository extends JpaRepository<Performer, Long> {

}
