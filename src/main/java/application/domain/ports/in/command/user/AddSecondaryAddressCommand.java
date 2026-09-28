package application.domain.ports.in.command.user;

/**
 * Comando para la operacion de AddSecondaryAddress.
 */
public record AddSecondaryAddressCommand(String buyerId, String address) {
}
