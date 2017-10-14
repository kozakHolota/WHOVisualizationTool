package org.http.rest_client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.localization.Languages;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalizationRestClient {
    private static String translatorUrl(Languages l, String englishWord) {
        return "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=" + l.getCode() + "&dt=t&q=" + englishWord;
    }

    private static String getFakeUserAgent() {
        return "Mozilla/5.0 (X11; Ubuntu; Linu…) Gecko/20100101 Firefox/55.0";
    }

    public static String translate(Languages l, String englishWord) throws IOException, ParseException {
        Log log = LogFactory.getLog(LocalizationRestClient.class);
        String fakeUserAgent = getFakeUserAgent();
        Pattern p = Pattern.compile("%");
        String percentReplacer = "%25";
        Matcher m = p.matcher(englishWord);
        englishWord = m.replaceAll(percentReplacer);
        p = Pattern.compile("\\s");
        m = p.matcher(englishWord);
        String spaceReplacer = "%20";
        englishWord = m.replaceAll(spaceReplacer);
        p = Pattern.compile("\\(");
        String lbReplacer = "%28";
        m = p.matcher(englishWord);
        englishWord = m.replaceAll(lbReplacer);
        p = Pattern.compile("\\)");
        String rbReplacer = "%29";
        m = p.matcher(englishWord);
        englishWord = m.replaceAll(rbReplacer);
        URL translationUrl = new URL(translatorUrl(l, englishWord));
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        HttpsURLConnection conn = (HttpsURLConnection) translationUrl.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("User-Agent", fakeUserAgent);
        OutputStream os = conn.getOutputStream();
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode() + ". Request URL: " + translationUrl.toString());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        String result = "";
        log.debug("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            log.debug(output);
            result = result.concat(output);
        }

        conn.disconnect();

        JSONParser parser = new JSONParser();
        JSONArray res = (JSONArray) parser.parse(result);
        log.debug("Taken JSON: ".concat(res.toJSONString()));

        return ((JSONArray) ((JSONArray) res.get(0)).get(0)).get(0).toString();
    }

    public static void main(String[] args) throws IOException, ParseException {
        Languages[] l = Languages.values();
        for (Languages languages : l) {
            System.out.println(languages.translate("mace"));
        }
    }
}
