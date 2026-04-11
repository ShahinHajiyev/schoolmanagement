package shako.schoolmanagement.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import shako.schoolmanagement.dto.RefreshTokenRequestDto;
import shako.schoolmanagement.dto.TokenResponseDto;
import shako.schoolmanagement.service.inter.RefreshTokenService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@RequestBody @Valid RefreshTokenRequestDto dto) {
        log.info("POST /auth/refresh");
        return ResponseEntity.ok(refreshTokenService.refreshAccessToken(dto));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String neptunCode = auth.getName();
        log.info("POST /auth/logout — user: {}", neptunCode);
        refreshTokenService.revokeRefreshToken(neptunCode);
        return ResponseEntity.ok("Logged out successfully");
    }
}
