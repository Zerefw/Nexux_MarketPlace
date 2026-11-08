package application.domain.ports.out.inventory;

import application.domain.model.entity.InventoryItem;
import application.domain.model.valueobject.Sku;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para la persistencia y consulta de registros de inventario por bodega y SKU.
 */
public interface InventoryRepositoryPort {

    /**
     * Guarda o actualiza un registro de inventario.
     *
     * @param item Entidad de dominio InventoryItem.
     * @return Registro de inventario persistido.
     */
    InventoryItem save(InventoryItem item);

    /**
     * Busca un registro de inventario por su identificador unico.
     *
     * @param id Identificador del registro de inventario.
     * @return Optional con el registro encontrado o vacio.
     */
    Optional<InventoryItem> findById(String id);

    /**
     * Busca el inventario de un SKU dentro de una bodega especifica.
     *
     * @param warehouseId Identificador de la bodega.
     * @param sku         Objeto de valor Sku.
     * @return Optional con el registro de inventario si existe.
     */
    Optional<InventoryItem> findByWarehouseIdAndSku(String warehouseId, Sku sku);

    /**
     * Obtiene todos los registros de inventario asociados a un SKU a traves de las distintas bodegas.
     *
     * @param sku Objeto de valor Sku.
     * @return Lista de registros de inventario para el SKU.
     */
    List<InventoryItem> findBySku(Sku sku);
}
