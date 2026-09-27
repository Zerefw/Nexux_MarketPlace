package application.adapter.in.web.dto;

import application.domain.model.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    private String id;
    private String fullName;
    private String email;
    private String role;

    public static UserResponseDTO fromDomain(User user) {
        if (user == null) return null;
        return UserResponseDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail().getAddress())
                .role(user.getRole().name())
                .build();
    }
}
