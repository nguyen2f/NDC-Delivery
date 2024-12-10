package com.ndc.deliverymanagement.util;
import com.ndc.deliverymanagement.model.Manager;
import com.ndc.deliverymanagement.model.Shipper;
import com.ndc.deliverymanagement.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    @Value("${jwt.secret}") // Lấy SECRET_KEY từ application.properties
    private String SECRET_KEY;

    public String extractPhoneNumber(String token) {
        String phoneNumber = extractClaim(token, Claims::getSubject);
        System.out.println("Extracted Phone Number: " + phoneNumber); // Log extracted phone number
        return phoneNumber;
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY) // Dùng SECRET_KEY để giải mã
                .parseClaimsJws(token)
                .getBody();
    }
    public boolean validateToken(String token, String phoneNumber) {
        final String extractedPhoneNumber = extractPhoneNumber(token);
        return extractedPhoneNumber.equals(phoneNumber) && !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String generateToken(User user) {
        // Tạo token với số điện thoại làm subject và role là "ROLE_USER"
        return Jwts.builder()
                .setSubject(user.getPhoneNumber()) // Lưu số điện thoại vào subject (dễ dàng truy vấn và kiểm tra)
                .claim("fullName", user.getFullName()) // Thêm thông tin tên người dùng
                .claim("role", "ROLE_USER")  // Thêm role vào token
                .setIssuedAt(new Date()) // Thời gian tạo token
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))  // Token hết hạn sau 1 ngày
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Ký với SECRET_KEY
                .compact();
    }


    public String generateTokenForManager(Manager manager) {
        return Jwts.builder()
                .setSubject(manager.getFullName())
                .claim("role", "ROLE_MANAGER")  // Lưu role "ROLE_MANAGER" cho Manager
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))  // Token hết hạn sau 1 ngày
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateTokenForShipper(Shipper shipper) {
        return Jwts.builder()
                .setSubject(shipper.getFullName())
                .claim("role", "ROLE_SHIPPER")  // Lưu role "ROLE_SHIPPER" cho Shipper
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))  // Token hết hạn sau 1 ngày
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
