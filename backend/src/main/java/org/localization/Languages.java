package org.localization;

import org.http.rest_client.LocalizationRestClient;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Objects;

public enum Languages {
    EN("en", "English"),
    UK("uk", "Українська");

    private String code, stringName;


    Languages(String code, String stringName) {
        this.code = code;
        this.stringName = stringName;
    }


    public String getCode() {
        return code;
    }

    public String getStringName() {
        return stringName;
    }

    public static boolean containsCode(String code) {
        boolean flag = false;
        Languages[] values = Languages.values();
        for (Languages langCode : values) {
            if (langCode.getCode().equals(code)) flag = true;
            break;
        }

        return flag;
    }

    public static Languages createFromCode(String code) {
        Languages l = null;
        for (Languages langCode : Languages.values()) {
            if (code.equals(langCode.getCode())) {
                l = langCode;
                break;
            }
        }
        return l;
    }

    public String translate(String englishWord) throws IOException, ParseException {
        if (Objects.equals(this.getCode(), Languages.EN.getCode())) return englishWord;

        return LocalizationRestClient.translate(this, englishWord);
    }
}
