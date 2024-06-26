package com.apartment.house.service;

import com.apartment.house.config.ApplicationConfig;
import com.apartment.house.dto.auth.LogoutRequestDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

  private final ApplicationConfig applicationConfig;
  private final Set<String> blacklist = new HashSet<>();

  public String generateToken(HashMap<String, Object> claims, UserDetails userDetails)
      throws ExpiredJwtException {
    return buildToken(claims, userDetails, applicationConfig.jwtExpiration);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) throws ExpiredJwtException {
    final String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  public String extractUsername(String token) throws ExpiredJwtException {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
      throws ExpiredJwtException {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) throws ExpiredJwtException, SignatureException {
    return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
        .getBody();
  }

  public boolean isTokenExpired(String token) throws ExpiredJwtException {
    return extractExpiration(token).before(new Date());
  }

  public void addToBlacklist(String token) {
    blacklist.add(token);
  }

  public boolean isBlacklisted(String token) {
    return blacklist.contains(token);
  }

  private Date extractExpiration(String token) throws ExpiredJwtException {
    return extractClaim(token, Claims::getExpiration);
  }

  private String buildToken(HashMap<String, Object> claims, UserDetails userDetails,
      long jwtExpiration) throws ExpiredJwtException {
    var authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .toList();

    return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
        .claim("authorities", authorities).setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .signWith(getSigningKey()).compact();
  }


  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(applicationConfig.secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public void addBlackListToken(LogoutRequestDTO logoutRequestDTO) {
    String token = logoutRequestDTO.getToken();
    String userEmail = extractUsername(token);
    if (userEmail == null) {
      throw new RuntimeException("User logged not found");
    }
    if (token == null) {
      throw new RuntimeException("Token not found");
    }
    if (isBlacklisted(token)) {
      throw new RuntimeException("User already logout");
    }
    addToBlacklist(token);
  }
}
