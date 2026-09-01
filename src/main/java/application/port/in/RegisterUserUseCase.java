package application.port.in;

import application.domain.model.entity.User;
import application.port.in.command.RegisterBuyerCommand;
import application.port.in.command.RegisterSellerCommand;

public interface RegisterUserUseCase {
    User registerBuyer(RegisterBuyerCommand command);
    User registerSeller(RegisterSellerCommand command);
}
