package pl.dawidsowa.mtabd.service.mapper;

import org.mapstruct.Mapper;
import pl.dawidsowa.mtabd.domain.Product;
import pl.dawidsowa.mtabd.service.dto.ProductDTO;

/**
 * Mapper for the entity Product and its DTO ProductDTO.
 */
@Mapper(componentModel = "spring", uses = {ServiceMapper.class})
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {



    default Product fromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
