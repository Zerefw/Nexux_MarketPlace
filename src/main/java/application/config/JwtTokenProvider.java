package application.config;

import application.domain.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Proveedor de tokens JWT para la autenticacion sin estado de la API REST.
 */
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret:unaClaveSecretaMuyLargaYSeguraDeAlMenos256BitsParaNexusMarketPlace2026}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:86400000}")
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un token JWT firmado para un usuario autenticado.
     *
     * @param user Entidad User autenticada.
     * @return Token JWT en formato compacto.
     */
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(user.getId())
                .claim("email", user.getEmail().getAddress())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Valida la firma y vigencia de un token JWT.
     *
     * @param token Token JWT a verificar.
     * @return true si el token es valido, false en caso contrario.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Extrae los claims contenidos en un token JWT valido.
     *
     * @param token Token JWT.
     * @return Objeto Claims con la informacion del usuario.
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
