package com.epam.campusTrainings.employeePayroll.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtEncoder jwtEncoder;

    public String generateTokens(Authentication authentication){
        Instant timestamp=Instant.now();

        String role=authentication.getAuthorities().toString();

        JwtClaimsSet jwtClaimsSet= JwtClaimsSet.builder()
                .issuer("Epam Systems")
                .issuedAt(timestamp)
                .subject(authentication.getName())
                .claim("scope", role)
                .expiresAt(timestamp.plus(5,ChronoUnit.MINUTES))
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }
}
