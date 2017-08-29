package org.http.rest_client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.storage_puller.DeathsAttributableToTheEnvironmentDataInsert;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * Created by chmel on 16.07.17.
 */
public class RestClient {
    private String restURI;
    private Log log = LogFactory.getLog(RestClient.class);
    public RestClient(String restURI){
        this.restURI = restURI;
    }

    private URL getURL(String path) throws MalformedURLException {
        return new URL(this.restURI + "/webapp" + path);
    }

    private JSONObject toJson(String response) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject result = (JSONObject) parser.parse(response);
        log.debug("Taken JSON: ".concat(result.toJSONString()));
        return result;
    }

    public JSONObject _get(String path, HashMap<String, String> params)
            throws IOException, ParseException, NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {

                    public java.security.cert.X509Certificate[] getAcceptedIssuers()
                    {
                        return null;
                    }
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                    {
                        //No need to implement.
                    }
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                    {
                        //No need to implement.
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HttpsURLConnection.setDefaultHostnameVerifier ((hostname, session) -> true);
        HttpsURLConnection conn = (HttpsURLConnection) this.getURL(path).openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setDoOutput( true );
        conn.setInstanceFollowRedirects( false );
        conn.setRequestMethod( "POST" );
        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty( "charset", "utf-8");
        String requestBody = "";
        for(String key: params.keySet()){
            requestBody += ""
                    .concat(key)
                    .concat("=")
                    .concat(params.get(key))
                    .concat("&");
        }
        conn.setRequestProperty( "Content-Length", Integer.toString(requestBody.length()));
        conn.setUseCaches( false );

        log.debug("Performing POST with parameters: ".concat(requestBody));

        OutputStream os = conn.getOutputStream();
        os.write(requestBody.getBytes());
        os.flush();
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        String result = "";
        log.debug("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
            result = result.concat(output);
        }

        conn.disconnect();

        return this.toJson(result);
    }
}
