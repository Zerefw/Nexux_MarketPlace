package application.domain.ports.in.command.user;

/**
 * Comando para la operacion de UpdateSellerTaxId.
 */
public record UpdateSellerTaxIdCommand(String sellerId, String taxId) {
}
