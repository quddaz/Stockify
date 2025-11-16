package com.quddaz.stock_simulator.domain.oauth;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class main {
    public static void main(String[] args) {
        // 1. HS512용 안전한 키 생성
        Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        // 2. Base64로 인코딩
        String base64Key = Encoders.BASE64.encode(secretKey.getEncoded());

        // 3. 출력
        System.out.println("Generated HS512 Base64 key: " + base64Key);
    }
}
