package ai.pnu.b03.minifarm.dto.user;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
