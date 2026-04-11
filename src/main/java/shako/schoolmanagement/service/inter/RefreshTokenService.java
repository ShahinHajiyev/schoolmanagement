package shako.schoolmanagement.service.inter;

import shako.schoolmanagement.dto.RefreshTokenRequestDto;
import shako.schoolmanagement.dto.TokenResponseDto;
import shako.schoolmanagement.entity.RefreshToken;
import shako.schoolmanagement.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    TokenResponseDto refreshAccessToken(RefreshTokenRequestDto dto);

    void revokeRefreshToken(String neptunCode);
}
