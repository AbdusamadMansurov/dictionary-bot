package pdp.bot.states;

public interface AdminState {
    String ADMIN_SHOW_MENU = "ADMIN_SHOW_MENU";

    // foydalanuvchi lar royhatini download qilish
    String USERS_DOWNLOAD = "USERS_DOWNLOAD";

    // barja foydalanuvchilarning tarjima qilgan sozlarini pdf fileda yuklash
    String TRANSLATE_WORDS_DOWNLOAD = "TRANSLATE_WORDS_DOWNLOAD";

    // barcha foydalanuvchilarga reklama va yangilik jonatish yuborish
    String SHARE_FOTO_OR_TEXT = "SHARE_FOTO_OR_TEXT";
    String ASK_BUTTON_INLINE = "SHARE_FOTO_OR_TEXT";
    String SHARE_URL = "SHARE_URL";
    String ASK_SENDING = "ASK_SENDING";

    // shikoyat va takliflarni korish
    String SHOW_MESSAGES = "SHOW_MESSAGES";
    String SEND_MESSAGE_OF_USER = "SEND_MESSAGE_OF_USER";



 }
