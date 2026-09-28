package application.domain.ports.in;

import application.domain.model.entity.User;
import application.domain.ports.in.command.RegisterBuyerCommand;
import application.domain.ports.in.command.RegisterSellerCommand;

public interface RegisterUserUseCase {
    User registerBuyer(RegisterBuyerCommand command);
    User registerSeller(RegisterSellerCommand command);
}
