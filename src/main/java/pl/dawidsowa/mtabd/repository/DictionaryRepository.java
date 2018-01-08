package pl.dawidsowa.mtabd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dawidsowa.mtabd.domain.Dictionary;


/**
 * Spring Data JPA repository for the Dictionary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {

}
