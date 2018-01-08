package pl.dawidsowa.mtabd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dawidsowa.mtabd.domain.DictionaryValue;


/**
 * Spring Data JPA repository for the DictionaryValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DictionaryValueRepository extends JpaRepository<DictionaryValue, Long> {

}
