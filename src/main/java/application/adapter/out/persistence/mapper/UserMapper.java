package application.adapter.out.persistence.mapper;

import application.adapter.out.persistence.entity.UserJpaEntity;
import application.domain.model.entity.User;
import application.domain.model.valueobject.Email;

public class UserMapper {

    public static UserJpaEntity toJpaEntity(User domain, String encodedPassword) {
        return UserJpaEntity.builder()
                .id(domain.getId())
                .identificationDocument(domain.getIdentificationDocument())
                .fullName(domain.getFullName())
                .email(domain.getEmail().getAddress())
                .role(domain.getRole())
                .active(domain.isActive())
                .createdAt(domain.getCreatedAt())
                .password(encodedPassword)
                .build();
    }

    public static User toDomainEntity(UserJpaEntity jpaEntity) {
        return User.builder()
                .id(jpaEntity.getId())
                .identificationDocument(jpaEntity.getIdentificationDocument())
                .fullName(jpaEntity.getFullName())
                .email(new Email(jpaEntity.getEmail()))
                .role(jpaEntity.getRole())
                .active(jpaEntity.isActive())
                .createdAt(jpaEntity.getCreatedAt())
                .build();
    }
}
