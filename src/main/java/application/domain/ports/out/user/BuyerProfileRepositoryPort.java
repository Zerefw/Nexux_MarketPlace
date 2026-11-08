package application.domain.ports.out.user;

import application.domain.model.entity.BuyerProfile;

import java.util.Optional;

/**
 * Puerto de salida para gestionar la persistencia de perfiles de comprador.
 */
public interface BuyerProfileRepositoryPort {

    /**
     * Guarda o actualiza un perfil de comprador.
     *
     * @param profile Entidad BuyerProfile a persistir.
     * @return Perfil de comprador persistido.
     */
    BuyerProfile save(BuyerProfile profile);

    /**
     * Busca un perfil de comprador por el identificador del usuario propietario.
     *
     * @param buyerId Identificador del usuario comprador.
     * @return Optional con el perfil encontrado o vacio.
     */
    Optional<BuyerProfile> findByBuyerId(String buyerId);
}
