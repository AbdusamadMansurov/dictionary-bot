package pdp.bot;


import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import pdp.bot.states.AdminState;
import pdp.bot.states.State;
import pdp.bot.states.UserState;
import pdp.database.Db;
import pdp.model.Api.Translate;
import pdp.model.TgUser;
import pdp.util.Btn;
import pdp.verification.Sms;
import pdp.verification.Verification;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class BotServise {


    @SneakyThrows
    public static SendMessage start(Update update) {
        String chatId = getChatId(update);
        TgUser user = getOrCreteUser(chatId);
        if (user.getState().equals(State.START))
            return shareContact(update);

        else if (user.getState().equals(AdminState.ADMIN_SHOW_MENU))
            return adminShowMenu(update);

        else if (user.getState().equals(UserState.USER_SHOW_MENU))
            return userShowMenu(user);
        else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("");
            sendMessage.setChatId(chatId);
            return sendMessage;
        }
    }

    public static SendMessage userShowMenu(TgUser user) {
        String chatId = user.getChatId();
        SendMessage sendMessage = new SendMessage();
        user.setState(UserState.USER_SHOW_MENU);
        saveUserChanges(user);

        sendMessage.setText("Botimizga hush kelibsiz ");
        sendMessage.setReplyMarkup(Btn.markupReplay(
                Btn.rowList(
                        Btn.row(Btn.button("Transleator", false, false))
                )
        ));
        sendMessage.setChatId(chatId);
        return sendMessage;
    }

    public static SendMessage adminShowMenu(Update update) {
        String chatId = getChatId(update);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Admin menu");
        sendMessage.setChatId(chatId);
        return sendMessage;
    }


    private static SendMessage shareContact(Update update) {
        String chatId = getChatId(update);
        TgUser user = getOrCreteUser(chatId);

        user.setState(State.SHARE_CONTACT);
        saveUserChanges(user);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        sendMessage.setText("Botdan foydalanish uchun Kontaktinigzni jonating");

        sendMessage.setReplyMarkup(Btn.markupReplay
                (Btn.rowList
                        (Btn.row
                                (Btn.button
                                        ("Share Contact", true, false)))));

        return sendMessage;
    }


    @SneakyThrows
    public static TgUser getOrCreteUser(String chatId) {
        for (TgUser user : Db.users) {
            if (user.getChatId().equals(chatId)) {
                return user;
            }
        }
        TgUser user = new TgUser(chatId, State.START);
        Db.users.add(user);
        return user;
    }


    public static String getChatId(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId().toString();
        }
        return String.valueOf(update.getMessage().getChatId());
    }


    public static SendMessage verification(String contact, TgUser tgUser) {
        SendMessage sendMessage = new SendMessage();
        String phoneNumber = checkPhoneNumber(contact);
        tgUser.setPhoneNumber(phoneNumber);
        Sms.sendSms(tgUser);
        sendMessage.setChatId(tgUser.getChatId());
        sendMessage.setText("Sizning telefon raqamingizga tasdiqlash kodi yuborildi iltimos kodni kiriting \n" + getCode(tgUser).getCode());
        sendMessage.setReplyMarkup(Btn.markupReplay(
                Btn.rowList(
                        Btn.row(
                                Btn.button("Re send code", false, false)
                        )
                )));
        tgUser.setState(State.ENTER_CODE);
        saveUserChanges(tgUser);


        return sendMessage;
    }

    public static Verification getCode(TgUser tgUser) {
        for (Verification verification : Db.verifications) {
            if (verification.getUser().equals(tgUser)) {
                return verification;
            }
        }
        Verification verification = new Verification(tgUser);
        Db.verifications.add(verification);
        return verification;

    }

    public static String checkPhoneNumber(String phoneNumber) {
        return phoneNumber.startsWith("+") ? phoneNumber : "+" + phoneNumber;
    }


    public static SendMessage changeLang(TgUser tgUser) {
        tgUser.setState(UserState.CHANGE_LANG);
        saveUserChanges(tgUser);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(tgUser.getChatId());
        sendMessage.setText("Tarjima qilmoqchi bolgan tilingizni tanlang");
        sendMessage.setReplyMarkup(Btn.inlineMarkup(
                Btn.inlineRow(Btn.inlineButton("\uD83C\uDDFA\uD83C\uDDF8 English " + (tgUser.getFromLang().equals("en") ? "✅" : ""),
                        "FROM:en"), Btn.inlineButton("\uD83C\uDDF9\uD83C\uDDF7 Turkish" + (tgUser.getToLang().equals("tr") ? "✅" : ""), "TO:tr")),

                Btn.inlineRow(Btn.inlineButton("\uD83C\uDDF7\uD83C\uDDFA Russion " + (tgUser.getFromLang().equals("ru") ? "✅" : ""),
                        "FROM:ru"), Btn.inlineButton("\uD83C\uDDFA\uD83C\uDDFF Uzbek " + (tgUser.getToLang().equals("uz") ? "✅" : ""), "TO:uz")),

                Btn.inlineRow(Btn.inlineButton("\uD83C\uDDFA\uD83C\uDDFF Uzbek " + (tgUser.getFromLang().equals("uz") ? "✅" : ""),
                        "FROM:uz"), Btn.inlineButton("\uD83C\uDDF7\uD83C\uDDFA Russion " + (tgUser.getToLang().equals("ru") ? "✅" : ""), "TO:ru")),

                Btn.inlineRow(Btn.inlineButton("\uD83C\uDDF9\uD83C\uDDF7 Turkish " + (tgUser.getFromLang().equals("tr") ? "✅" : ""),
                        "FROM:tr"), Btn.inlineButton("\uD83C\uDDFA\uD83C\uDDF8 English " + (tgUser.getToLang().equals("en") ? "✅" : ""), "TO:en")),
                Btn.inlineRow(Btn.inlineButton(tgUser.getFromLang().equals("") || tgUser.getToLang().equals("") ? "✔️NEXT✔️" : "✅NEXT✅", "NEXT"))
        ));
        return sendMessage;

    }

    public static void saveUserChanges(TgUser changedUser) {
        for (TgUser user : Db.users) {
            if (user.getChatId().equals(changedUser.getChatId())) {
                user = changedUser;
            }
        }
    }

    public static SendMessage translator(Update update, TgUser user) {
        user.setState(UserState.TRANSLATE_MENU);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(getChatId(update));
        sendMessage.setText(user.getFromLang() + "  -->  " + user.getToLang() + "\n\n So'z yoki Gap farqi yoq yozing...");
        sendMessage.setReplyMarkup(Btn.markupReplay(Btn.rowList(Btn.row(Btn.button(" ↩️ Ortga ↩️", false, false)))));
        return sendMessage;
    }

    public static SendMessage translateMenu(TgUser tgUser, String text) throws IOException, InterruptedException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(tgUser.getChatId());
        sendMessage.setText(api(text,tgUser));
        return sendMessage;
    }

    public static String api(String str, TgUser user) throws IOException, InterruptedException {

        String tr = "{\n" +
                "    \"q\": \"" + str + " \",\n" +
                "    \"source\": \"" + user.getFromLang() + "\",\n" +
                "    \"target\": \"" + user.getToLang() + "\"\n" +
                "}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://deep-translate1.p.rapidapi.com/language/translate/v2"))
                .header("content-type", "application/json")
                .header("x-rapidapi-host", "deep-translate1.p.rapidapi.com")
                .header("x-rapidapi-key", "c209d9cafbmsheff34ff06f1465fp164db8jsn8dcf1e7a384a")
                .method("POST", HttpRequest.BodyPublishers.ofString(tr))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();
        Translate translate = gson.fromJson(response.body(), Translate.class);
        return translate.getData().getTranslations().getTranslatedText();

    }
}

