package org.webcontent;

import org.data_getters.model.LoginStatus;
import org.http.rest_client.RestClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by chmel on 08.06.17.
 */

@Controller
public class WebContentServletController {
    private static ApplicationContext context;
    static {
        context = new ClassPathXmlApplicationContext("beans/RESTClientBean.xml");
    }
    RestClient rc = (RestClient) context.getBean("restClient");

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String indexPage(ModelMap model,
                            @RequestParam(value = "username", required = false) String userName,
                            @RequestParam(value = "__token", required = false) String token){
        if (userName == null) model.addAttribute("dt", "Not Logged");
        else model.addAttribute("dt", "Logged");
        return "start";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexPage(ModelMap model){
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap model) {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(ModelMap model,
                        @RequestParam(value = "username", required = true) String userName,
                        @RequestParam(value = "password", required = true) String password,
                        @RequestParam(value = "lang", required = true) String lang) throws IOException, ParseException, KeyManagementException, NoSuchAlgorithmException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", userName);
        params.put("password", password);
        AtomicReference<JSONObject> res = new AtomicReference<>(this.rc._get("/rest/login", params));
        String loginStatus = (String) res.get().get("status");
        if (loginStatus.equals(LoginStatus.FAILURE.toString())) return "login";
        model.addAttribute("username", userName);
        model.addAttribute("token", res.get().get("hash_code"));
        model.addAttribute("json", res.get().toJSONString());
        model.addAttribute("lang", lang);
        return "workspace";
    }

    @RequestMapping(value = "/check_login", method = RequestMethod.POST)
    public String checkLogin(ModelMap model,
                             @RequestParam(value = "username", required = true) String userName,
                             @RequestParam(value = "__token", required = true) String token) throws KeyManagementException, NoSuchAlgorithmException, ParseException, IOException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", userName);
        params.put("__token", token);
        AtomicReference<JSONObject> res = new AtomicReference<>(this.rc._get("/rest/check_login", params));
        String loginStatus = (String) res.get().get("status");
        if (loginStatus.equals(LoginStatus.FAILURE.toString())) return "login";
        model.addAttribute("username", userName);
        model.addAttribute("token", token);
        model.addAttribute("json", res.get().toJSONString());
        return "workspace";
    }

    @RequestMapping(value = "/jquery", method = RequestMethod.GET)
    public String getJQuery(ModelMap model){
        return "jquery-3.2.1.min";
    }
}
