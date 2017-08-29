package org.http.data_getter.requester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by chmel on 29.12.16.
 */
public class DataRequester {
    private String URI;

    public DataRequester(String URI) {
        this.URI = URI;
    }

    private URL getUrl() throws MalformedURLException {
        return new URL(this.URI);
    }

    ;

    public String getData() throws IOException {
        URL url = getUrl();
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(httpCon.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        return response.toString();
    }

    ;
}
