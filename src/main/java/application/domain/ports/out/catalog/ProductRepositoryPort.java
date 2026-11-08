package application.domain.ports.out.catalog;

import application.domain.model.entity.Product;
import application.domain.model.valueobject.Sku;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para la persistencia y consulta de productos del catalogo.
 */
public interface ProductRepositoryPort {

    /**
     * Guarda o actualiza un producto en el catalogo.
     *
     * @param product Entidad de dominio Product.
     * @return Producto persistido.
     */
    Product save(Product product);

    /**
     * Busca un producto por su identificador unico.
     *
     * @param id Identificador del producto.
     * @return Optional con el producto si existe.
     */
    Optional<Product> findById(String id);

    /**
     * Busca un producto por su codigo SKU unico.
     *
     * @param sku Objeto de valor Sku.
     * @return Optional con el producto si existe.
     */
    Optional<Product> findBySku(Sku sku);

    /**
     * Comprueba si existe un producto registrado con el SKU indicado.
     *
     * @param sku Objeto de valor Sku.
     * @return true si existe, false en caso contrario.
     */
    boolean existsBySku(Sku sku);

    /**
     * Recupera todos los productos que se encuentran activos en el catalogo.
     *
     * @return Lista de productos activos.
     */
    List<Product> findAllActive();
}
