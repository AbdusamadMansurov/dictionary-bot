package pdp.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import pdp.bot.states.State;
import pdp.bot.states.UserState;
import pdp.model.TgUser;


public class DIctotinaryBot extends TelegramLongPollingBot {
    private static final String ADMIN_ID = "919224843";

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {


        if (update.hasMessage()) {
            TgUser tgUser = BotServise.getOrCreteUser(BotServise.getChatId(update));
            // contact
            if (update.getMessage().hasContact()) {
                if (tgUser.getState().equals(State.SHARE_CONTACT)) {

                    execute(BotServise.verification(String.valueOf(update.getMessage().getContact()), tgUser));
                }

            } else {
                //Start
                String text = update.getMessage().getText();
                if (text.equals("/start") && tgUser.getState().equals(State.START)) {
                    execute(BotServise.start(update));
                } else {

                    if (tgUser.getState().equals(State.ENTER_CODE)) {
                        if (text.equals("Re send code")) {
                            execute(BotServise.verification(tgUser.getPhoneNumber(), tgUser));

                        } else {
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(BotServise.getChatId(update));
                            if (BotServise.getCode(tgUser).getCode().equals(text)) {
                                if (tgUser.getChatId().equals(ADMIN_ID)) {
                                    execute(BotServise.adminShowMenu(update));
                                } else {
                                    execute(BotServise.userShowMenu(tgUser));
                                }
                            } else {
                                sendMessage.setText("Kod xato qaytadan urining");
                                execute(sendMessage);
                            }
                        }
                    } else if (tgUser.getState().equals(UserState.USER_SHOW_MENU)) {
                        deleteMessage(tgUser);
                        if (text.equals("Transleator")) {
                            execute(BotServise.changeLang(tgUser));
                        }
                    } else if (tgUser.getState().equals(UserState.CHANGE_LANG)) {
                        DeleteMessage deleteMessage = new DeleteMessage(tgUser.getChatId(), update.getMessage().getMessageId());
                        execute(deleteMessage);
                    } else if (tgUser.getState().equals(UserState.TRANSLATE_MENU)) {
                        if (text.equals("↩️ Ortga ↩️")) {
                            tgUser.setState(UserState.CHANGE_LANG);
                            execute(BotServise.changeLang(tgUser));
                            deleteMessage(tgUser);
                        } else {

                            execute(BotServise.translateMenu(tgUser, text));
                        }
                    }
                }
            }
        } else if (update.hasCallbackQuery()) {
            TgUser tgUser = BotServise.getOrCreteUser(BotServise.getChatId(update));

            String data = update.getCallbackQuery().getData();

            if (tgUser.getState().equals(UserState.CHANGE_LANG)) {
                if (data.startsWith("FROM:")) {
                    if (!tgUser.getToLang().equals(data.substring(data.indexOf(":") + 1))) {

                        tgUser.setFromLang(data.substring(data.indexOf(":") + 1));
                        DeleteMessage deleteMessage = new DeleteMessage(tgUser.getChatId(), update.getCallbackQuery().getMessage().getMessageId());
                        execute(deleteMessage);
                        execute(BotServise.changeLang(tgUser));
                    }
                } else if (data.startsWith("TO:")) {
                    if (!tgUser.getFromLang().equals(data.substring(data.indexOf(":") + 1))) {
                        tgUser.setToLang(data.substring(data.indexOf(":") + 1));
                        DeleteMessage deleteMessage = new DeleteMessage(tgUser.getChatId(), update.getCallbackQuery().getMessage().getMessageId());
                        execute(deleteMessage);

                        execute(BotServise.changeLang(tgUser));
                    }
                } else if (data.equals("NEXT")) {
                    if (!tgUser.getFromLang().equals("") && !tgUser.getToLang().equals("")) {
                        execute(BotServise.translator(update, tgUser));
                        DeleteMessage deleteMessage = new DeleteMessage(BotServise.getChatId(update), update.getCallbackQuery().getMessage().getMessageId());
                        execute(deleteMessage);
                    }
                }

            }


//                    Language []from = Language.values();
//            tgUser.setFromLang(data.substring(data.indexOf(":") + 1));
//            BotServise.saveUserChanges(tgUser);
//            DeleteMessage deleteMessage = new DeleteMessage(tgUser.getChatId(), update.getCallbackQuery().getMessage().getMessageId());
//            execute(deleteMessage);

            //             else if (data.startsWith("TO:")) {
            //                tgUser.setToLang(data.substring(data.indexOf(":") + 1));
            //                BotServise.saveUserChanges(tgUser);
            //                System.out.println("asdasd");
            //
            //            }


        }
    }

    @SneakyThrows
    public void deleteMessage(TgUser user) {
        SendMessage sendMessageRemove = new SendMessage();
        sendMessageRemove.setChatId(user.getChatId());
        sendMessageRemove.setText(".");
        sendMessageRemove.setReplyMarkup(new ReplyKeyboardRemove(true));
        Message message = execute(sendMessageRemove);
        DeleteMessage deleteMessage = new DeleteMessage(user.getChatId(), message.getMessageId());
        execute(deleteMessage);
    }


    @Override
    public String getBotUsername() {
        return "dictionary_in_java_bot";
    }

    @Override
    public String getBotToken() {
        return "5420235844:AAEafV8OcipLVMpzdn_uuB0ViN5GKWwAPO0";
    }

}
