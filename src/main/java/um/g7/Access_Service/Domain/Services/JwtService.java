package um.g7.Access_Service.Domain.Services;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // @Value("${security.jwt.secret-key}")
    private String secretKey = "POSAJOAIGJOGDIHasldljsdkjFDLDGOhi";
    // @Value("${security.jwt.expiration-time}")
    private Integer expirationTime = 100000;

    private final String ADMINTYPE = "admin";
    private final String DOORTYPE = "raspi";

    private final JwtParser jwtParser;

    public JwtService() {
        this.jwtParser = Jwts.parser().verifyWith(getKey()).build();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateAdminToken(String email) {
        return Jwts.builder()
                .subject(email)
                .claim("type", ADMINTYPE)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getKey())
                .compact();
    }

    public boolean isValidAdminToken(String token) {
        Jws<Claims> claims = parseToken(token);
        if(claims == null) return false;

        return claims.getPayload().get("type").equals(ADMINTYPE);
    }

    public String generateDoorToken(String email) {
        return Jwts.builder()
                .subject(email)
                .claim("type", DOORTYPE)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 864000000))
                .signWith(getKey())
                .compact();
    }

    public boolean isValidDoorToken(String token) {
        Jws<Claims> claims = parseToken(token);
        if(claims == null) return false;

        return claims.getPayload().get("type").equals(DOORTYPE);
    }

    public Jws<Claims> parseToken(String token) {
        try {
            return jwtParser.parseSignedClaims(token);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public String getEmailFromToken(String token) {
        String email = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();

        return email;
    }

}
