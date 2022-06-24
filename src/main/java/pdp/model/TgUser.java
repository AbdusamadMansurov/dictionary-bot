package pdp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TgUser {
    private UUID id = UUID.randomUUID();
    private String chatId;
    private String fio;
    private String username;
    private String phoneNumber;
    private String State = pdp.bot.states.State.START  ;

    //languages
    private String fromLang="";
    private String toLang="";

    public TgUser(String chatId, String state) {
        this.chatId = chatId;
        State = state;
    }

}
