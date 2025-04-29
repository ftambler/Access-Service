package um.g7.Access_Service.Domain.Services;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    
    // @Value("${security.jwt.secret-key}")
    private String secretKey = "POSAJOAIGJOGDIHasldljsdkjFDLDGOhi";
    // @Value("${security.jwt.expiration-time}")
    private Integer expirationTime = 10000;

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() * expirationTime))
                .signWith(getKey())
                .compact();
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
