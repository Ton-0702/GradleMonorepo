package com.kits.apichatapp.service.Authen;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    private final Logger logger = LoggerFactory.getLogger(JwtService.class);


    // generate token don't have additional claims (easy)
    public String generateToken(UserDetails userDetails) {
            return generateToken(new HashMap<>(), userDetails);
    }

    // generate token have additional claims (can custom add more info claims to token)
    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails) {

        return Jwts.builder()
                .claims(extractClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis())) // Release time at token create
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // valid for 24 minutes
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact(); // generate and return token
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // subject: email or username
    }

    // extract specific claim object
    // Function<Claims, T> claimsResolver: Một hàm lambda hoặc method reference, nhận vào một Claims object và trả về một giá trị kiểu T.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims2(token); // extract all info claims from JWT
        return claimsResolver.apply(claims); // deal claims object and return value type T.
    }

    // giải mã và Trích xuất thông tin token (trong claims token)
//    private String extractAllClaims(String token){
//        return Jwts.parser() // Tạo một đối tượng JwtParser để xác minh và giải mã JWT token.
//                .verifyWith((SecretKey) getSignInKey()) // Đặt khóa bí mật (secret) để xác thực JWT token. Khóa này sẽ được sử dụng để xác minh tính hợp lệ của JWT token.
//                .build() // hoàn tất cấu hình JwtParser và trả về một đối tượng JwtParser đã được cấu hình hoàn chỉnh.
//                .parseSignedClaims(token) // Giải mã và xác minh tính hợp lệ của JWT token. trả về đối tượng Jws<Claims> chứa thông tin JWT token
//                .getPayload() // truy xuất phần nội dung JWT token (còn dc gọi payload) dưới dạng đối tượng Claims. Chứa các thông tin user
//                .getSubject(); // trả về giá trị cụ thể của claim subject trong payload. Thường thông tin về ng dùng: ví dụ như ID hoặc tên đăng nhập
//    }

    private Claims extractAllClaims2(String token){
        return Jwts.parser() // Tạo một đối tượng JwtParser để xác minh và giải mã JWT token.
                .verifyWith((SecretKey) getSignInKey()) // Đặt khóa bí mật (secret) để xác thực JWT token. Khóa này sẽ được sử dụng để xác minh tính hợp lệ của JWT token.
                .build() // hoàn tất cấu hình JwtParser và trả về một đối tượng JwtParser đã được cấu hình hoàn chỉnh.
                .parseSignedClaims(token) // Giải mã và xác minh tính hợp lệ của JWT token. trả về đối tượng Jws<Claims> chứa thông tin JWT token
                .getPayload();// truy xuất phần nội dung JWT token (còn dc gọi payload) dưới dạng đối tượng Claims. Chứa các thông tin user
    }

    // Kiểm tra validation JWT token
    public boolean validateToken(String token) {
        try {
            // Jws là interface cung cấp bơi libary JWT trong đó chứa các Claims
            Jws<Claims> claims =  Jwts.parser().verifyWith((SecretKey) getSignInKey()).build().parseSignedClaims(token); //xác định thông tin về người dùng, chẳng hạn như tên, vai trò, thời gian cấp token, v.v.
            return !claims.getPayload()
                    .getExpiration() //Lấy thời gian hết hạn (expiration time) của JWT.
                    .before(new Date()); // Kiểm tra xem thời gian hết hạn có trước thời điểm hiện tại hay không.
        }catch (SecurityException | MalformedJwtException | IllegalArgumentException exception) {
            logger.info("JWT TOKEN NOT VALID");
        }catch (ExpiredJwtException exception) {
            logger.info("JWT TOKEN EXPIRATION");
        } catch (UnsupportedJwtException exception) {
            logger.info("JWT TOKEN NOT SUPPORT");
        }
        return false;
    }

    //giải mã chuỗi SECRET_KEY từ Base64 thành một mảng byte,
    // sau đó sử dụng mảng byte đó để tạo một khóa bí mật (secret key) dùng trong quá trình xác thực JWT token.
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
