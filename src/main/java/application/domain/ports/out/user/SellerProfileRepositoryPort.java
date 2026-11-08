package application.domain.ports.out.user;

import application.domain.model.entity.SellerProfile;

import java.util.Optional;

/**
 * Puerto de salida para gestionar la persistencia de perfiles de vendedor.
 */
public interface SellerProfileRepositoryPort {

    /**
     * Guarda o actualiza un perfil de vendedor.
     *
     * @param profile Entidad SellerProfile a persistir.
     * @return Perfil de vendedor persistido.
     */
    SellerProfile save(SellerProfile profile);

    /**
     * Busca un perfil de vendedor por el identificador del usuario propietario.
     *
     * @param sellerId Identificador del usuario vendedor.
     * @return Optional con el perfil encontrado o vacio.
     */
    Optional<SellerProfile> findBySellerId(String sellerId);
}
