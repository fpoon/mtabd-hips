package pl.dawidsowa.mtabd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.dawidsowa.mtabd.domain.Product;

import java.util.List;

/**
 * Spring Data JPA repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select distinct product from Product product left join fetch product.services")
    List<Product> findAllWithEagerRelationships();

    @Query("select product from Product product left join fetch product.services where product.id =:id")
    Product findOneWithEagerRelationships(@Param("id") Long id);

}
