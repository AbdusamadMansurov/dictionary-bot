package pdp.verification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pdp.model.TgUser;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Verification {
    private UUID id=UUID.randomUUID();
    private TgUser user;
    private String code;

    public Verification(TgUser user) {
        this.user = user;
    }
}
