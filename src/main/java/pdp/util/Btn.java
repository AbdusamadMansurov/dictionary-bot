package pdp.util;


import org.telegram.telegrambots.meta.api.objects.replykeyboard.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Btn {

    @SafeVarargs
    public static InlineKeyboardMarkup inlineMarkup(List<InlineKeyboardButton>... rows) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineButton = new ArrayList<>();
        inlineButton.addAll(Arrays.asList(rows));
        markup.setKeyboard(inlineButton);
        return markup;
    }

    public static InlineKeyboardButton inlineButton(String text, String callbackData) {
        InlineKeyboardButton btn = new InlineKeyboardButton();
        btn.setText(text);
        btn.setCallbackData(callbackData);
        return btn;
    }

    public static List<InlineKeyboardButton> inlineRow(InlineKeyboardButton... inlineKeyboardButtons) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.addAll(Arrays.asList(inlineKeyboardButtons));
        return row;
    }


    public static ReplyKeyboardMarkup markupReplay(List<KeyboardRow> rows) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setSelective(true);
        markup.setKeyboard(rows);
        markup.setOneTimeKeyboard(true);


        return markup;
    }

    public static List<KeyboardRow> rowList(KeyboardRow... keyboardRows) {
        List<KeyboardRow> keyboardRows1 = new ArrayList<>();
        keyboardRows1.addAll(Arrays.asList(keyboardRows));
        return keyboardRows1;
    }

    public static KeyboardRow row(KeyboardButton... buttons) {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.addAll(Arrays.asList(buttons));
        return keyboardRow;
    }

    public static KeyboardButton button(String text, boolean contact, boolean location) {
        KeyboardButton button = new KeyboardButton();
        button.setText(text);
        button.setRequestContact(contact);
        button.setRequestLocation(location);
        return button;
    }


}
