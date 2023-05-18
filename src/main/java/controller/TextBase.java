package controller;

import java.util.HashMap;

public class TextBase {
    private static final HashMap<String, String> translationMap = new HashMap<>();

    static {
        translationMap.put("all", "همه");
        //translationMap.put("همه", "all");
        translationMap.put("user info", "مشخصات بازیکن");
        //translationMap.put("مشخصات بازیکن", "user info");
        translationMap.put("difficulty", "درجۀ سختی");
        //translationMap.put("درجۀ سختی", "difficulty");
        translationMap.put("change my username", "تغییر نام کاربری");
        //translationMap.put("تغییر نام کاربری", "change my username");
        translationMap.put("change my password", "تغییر رمز عبور");
        //translationMap.put("تغییر رمز عبور", "change my username");
        translationMap.put("from default avatars", "از تصاویر پیش فرض");
        //translationMap.put("از تصاویر پیش فرض", "from default avatars");
        translationMap.put("upload avatar", "آپلود عکس");
        //translationMap.put("آپلود عکس", "upload avatar");
        translationMap.put("highscore", "امتیاز");
        translationMap.put("time", "زمان");
        translationMap.put("level", "درجۀ سختی");
        translationMap.put("toggle", "تاگل");
    }

    //TODO update texts

    public static String getCurrentText(String input) {
        if (DataBase.isLangEnglish())
            return input;
        return translationMap.getOrDefault(input, "!");
    }
}