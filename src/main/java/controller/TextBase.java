package controller;

import java.util.HashMap;

public class TextBase {
    private static final HashMap<String, String> translationMap = new HashMap<>();

    static {
        translationMap.put("all", "همه");
        translationMap.put("user info", "مشخصات بازیکن");
        translationMap.put("difficulty", "درجۀ سختی");
        translationMap.put("change my username", "تغییر نام کاربری");
        translationMap.put("change my password", "تغییر رمز عبور");
        translationMap.put("from default avatars", "از تصاویر پیش فرض");
        translationMap.put("upload avatar", "آپلود عکس");
        translationMap.put("highscore", "امتیاز");
        translationMap.put("time", "زمان");
        translationMap.put("level", "درجۀ سختی");
        translationMap.put("toggle", "تاگل");
        translationMap.put("balls count", "تعداد توپ ها");
        translationMap.put("username field is empty!", "فیلد نام کاربری خالیست!");
        translationMap.put("password field is empty!", "فیلد رمز خالیست!");
        translationMap.put("you already have this username!", "از قبل این نام کاربری را دارید!");
        translationMap.put("username unavailable!", "نام کاربری ذر دسترس نیست");
        translationMap.put("username length should between 5 and 15!", "طول نام کاربری باید بین 5 و 15 حرف باشد!");
        translationMap.put("username must start with @!", "نام کاربری باید با @ شروع شود!");
        translationMap.put("username must only have lowercase letters and numbers after @", "در نام کاربری بعد @ فقط باید حروف کوچک و اعداد بیاید");
        translationMap.put("username must have a lowercase letter after @", "نام کاربری بعد @ باید یک حرف کوچک داشته باشد");
        translationMap.put("username successfully changed!", "نام کاربری عوض شد!");
        translationMap.put("wrong current password!", "رمز کنونی معتبر نیست");
        translationMap.put("successfully changed password!", "رمز عوض شد!");
        translationMap.put("password length must be at least 5!", "طول رمز باید حداقل 5 باشد");
        translationMap.put("password length must be at most 15!", "طول رمز باید حداکثر 15 باشد");
        translationMap.put("password must contain numbers!", "رمز باید شامل اعداد هم بشود!");
        translationMap.put("password must contain special characters(-@#$%^&*!)!", "رمز باید شامل کاراکتر های خاص -@#$%^&*! داشته باشد!");
        translationMap.put("password must contain lowercase letters!", "رمز باید شامل حروف کوچک هم بشود!");
        translationMap.put("password must contain uppercase letters!", "رمز باید شامل حروف بزرگ هم بشود!");
        translationMap.put("score", "امتیاز");
        translationMap.put("YOU WON!", "برنده شدید!");
        translationMap.put("YOU LOST!", "باختید!");
        translationMap.put("exit", "خروج");
        translationMap.put("track toggle", "موسیقی/صدا");
        translationMap.put("save", "ذخیره");
        translationMap.put("restart", "از اول");
        translationMap.put("help", "کمک");
        translationMap.put("game controls", "دکمه های بازی");
        translationMap.put("throw balls", "پرتاب توپ");
        translationMap.put("Freeze", "یخ زدن");
        translationMap.put("pause", "توقف");
        translationMap.put("movement", "حرکت");
        translationMap.put("arrow keys", "دکمه های جهت");
        translationMap.put("current password", "رمز کنونی");
        translationMap.put("new username", "نام کاربری جدید");
        translationMap.put("new password", "رمز جدید");
    }

    public static String getCurrentText(String input) {
        if (DataBase.isLangEnglish())
            return input;
        return translationMap.getOrDefault(input, "!");
    }
}