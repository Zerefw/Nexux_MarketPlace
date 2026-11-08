package application.domain.ports.out.warehouse;

import application.domain.model.entity.Warehouse;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para la persistencia y consulta de bodegas fisicas.
 */
public interface WarehouseRepositoryPort {

    /**
     * Guarda o actualiza una bodega en el sistema.
     *
     * @param warehouse Entidad de dominio Warehouse.
     * @return Bodega persistida.
     */
    Warehouse save(Warehouse warehouse);

    /**
     * Busca una bodega por su identificador unico.
     *
     * @param id Identificador de la bodega.
     * @return Optional con la bodega encontrada o vacio.
     */
    Optional<Warehouse> findById(String id);

    /**
     * Lista todas las bodegas activas.
     *
     * @return Lista de bodegas activas.
     */
    List<Warehouse> findAllActive();
}
