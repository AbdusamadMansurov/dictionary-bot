package pdp.verification;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import pdp.database.Db;
import pdp.model.TgUser;


public class Sms {
    private static final String SID = "ACb768322ce26fea1753f9f2f5fe131351";
    private static final String TOKEN = "a7ef9411ae163a19c49e622141a73078";
    private static final String FROM_PHONE = "+17752568643";

    public static boolean sendSms(TgUser user) {

        String code = String.valueOf((int)(Math.random() * (99999 - 10000)) + 10000);
        try {
            Twilio.init(SID, TOKEN);
            Message message = Message.creator(
                    new PhoneNumber(user.getPhoneNumber()),
                    new PhoneNumber(FROM_PHONE),
                    "Tasdiqlash kodi" + code).create();

            Verification verification = new Verification(user);
            verification.setCode(code);
            Db.verifications.add(verification);
            return true;
        } catch (Exception e) {
            Verification verification = new Verification(user);
            verification.setCode(code);
            Db.verifications.add(verification);
            return false;
        }

    }
}
