package com.kits.apichatapp.service.Authen;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    public String extractUsername(String token) {
        return null;
    }

    // extract specific claim object
    // claimsResolver: get parameter Claims object and return value type T
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims2(token); // extract all info claims from JWT
        return claimsResolver.apply(claims);
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

    //giải mã chuỗi SECRET_KEY từ Base64 thành một mảng byte,
    // sau đó sử dụng mảng byte đó để tạo một khóa bí mật (secret key) dùng trong quá trình xác thực JWT token.
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
