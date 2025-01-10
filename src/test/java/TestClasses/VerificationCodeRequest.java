package TestClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCodeRequest {

    private String email;
    private String code;
    private boolean authenticated;
    private LocalDateTime codeExpiration;
}
