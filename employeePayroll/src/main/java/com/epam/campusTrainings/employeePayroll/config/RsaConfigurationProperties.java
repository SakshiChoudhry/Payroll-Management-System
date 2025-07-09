package com.epam.campusTrainings.employeePayroll.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
public record RsaConfigurationProperties(RSAPrivateKey privateKey, RSAPublicKey publicKey) { }
