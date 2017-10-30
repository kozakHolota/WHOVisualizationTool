package org.webcontent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.data_getters.DataGetter;
import org.data_getters.dao.AppLogin;
import org.data_getters.dao.NecessaryDataHelper;
import org.data_getters.dao.StatisticClauseFields;
import org.data_getters.dao.TokenAuthDao;
import org.data_getters.model.LoginStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.localization.Languages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.storage_puller.DBPuller;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.data_getters.dao.NecessaryDataHelper.*;

/**
 * Created by chmel on 18.02.17.
 */
@Controller
@RequestMapping("/rest")
public class BackendServletController {

    private JSONObject formSourceUpdateResponse(String status, java.util.Date date) {
        JSONObject jo = new JSONObject();
        jo.put("source_update_status", status);
        jo.put("date", date.toString());

        return jo;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/source_update")
    @ResponseBody
    public ResponseEntity<String> updateSources(ModelMap model,
                                                @RequestParam(value = "username", required = true) String userName,
                                                @RequestParam(value = "__token", required = true) String token) throws ParseException {
        Log log = LogFactory.getLog(BackendServletController.class);
        HashMap<String, String> tokenAuthRes = new TokenAuthDao(userName, token).checkAdminAccess();
        JSONObject res = new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus hs = HttpStatus.OK;

        if (tokenAuthRes.get("status").equals(LoginStatus.FAILURE.toString())) {
            res.putAll(tokenAuthRes);
        } else {
            ApplicationContext dataGettersContext = new ClassPathXmlApplicationContext("beans/DataGettersBeans.xml");
            ArrayList<Hashtable<String, String>> death_by_environment_statistic = (ArrayList<Hashtable<String, String>>) dataGettersContext.getBean("deathByEnvironmentStatistics");
            ArrayList<Hashtable<String, String>> life_expecatncy_statistic = (ArrayList<Hashtable<String, String>>) dataGettersContext.getBean("lifeExpectancyStatustics");
            ArrayList<Hashtable<String, String>> tobacco_usage_statistic = (ArrayList<Hashtable<String, String>>) dataGettersContext.getBean("tobaccoUseDataByCountryStatistics");
            ApplicationContext dataWriterContext = new ClassPathXmlApplicationContext("beans/DataWriter.xml");
            DBPuller dp = (DBPuller) dataWriterContext.getBean("DPPuller");
            try {
                log.info("Updating Death by Environment Statistic");
                dp.insert("death_by_environment_statistics", death_by_environment_statistic);
                log.info("Updating Life Expectancy Statistic");
                dp.insert("life_statistic", life_expecatncy_statistic);
                log.info("Updating Tobacco Usage Statistic");
                dp.insert("tobacco_usage_statistic", tobacco_usage_statistic);
                res = this.formSourceUpdateResponse("Success", new java.util.Date());
            } catch (Exception ex) {
                model.addAttribute("source_update_status", "Failure");
                model.addAttribute("date", new java.util.Date().toString());
                res = this.formSourceUpdateResponse("Failure", new java.util.Date());
                res.put("exception", ex.getMessage());
                hs = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            res.putAll(tokenAuthRes);
        }

        return new ResponseEntity<>(res.toJSONString(), responseHeaders, hs);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> login(ModelMap loginModel,
                                        @RequestParam(value = "username", required = true) String userName,
                                        @RequestParam(value = "password", required = true) String password) {
        AppLogin al = new AppLogin(userName, password);
        HashMap<String, String> loginResult = al.checkLogin();
        JSONObject res = new JSONObject();
        res.put("status", loginResult.get("status"));
        res.put("hash_code", loginResult.get("hash_code"));
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        String resultJson = res.toJSONString();
        return new ResponseEntity<>(resultJson, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/check_login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> checkLogin(ModelMap loginModel,
                                        @RequestParam(value = "username", required = true) String userName,
                                        @RequestParam(value = "__token", required = true) String token) {
        JSONObject jo = new JSONObject();
        TokenAuthDao _tokenAuthDao = new TokenAuthDao(userName, token);
        HashMap<String, String> tokenAuthRes = _tokenAuthDao.checkLogin();
        jo.put("status", tokenAuthRes.get("status"));
        jo.put("reason", tokenAuthRes.get("reason"));
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(jo.toJSONString(), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> logout(ModelMap model,
                                         @RequestParam(value = "username", required = true) String userName,
                                         @RequestParam(value = "__token", required = true) String token){
        JSONObject jo = new JSONObject();
        TokenAuthDao _tokenAuthDao = new TokenAuthDao(userName, token);
        HashMap<String, String> tokenAuthRes = _tokenAuthDao.checkLogin();
        if (tokenAuthRes.get("status").equals(LoginStatus.SUCCESS.toString())){
            _tokenAuthDao.clearHash();
            jo.put("status", LoginStatus.LOGOUT.toString());
        } else {
            jo.put("status", tokenAuthRes.get("status"));
            jo.put("reason", tokenAuthRes.get("reason"));
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jo.toJSONString(), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/get_chart", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> chart_test(
            ModelMap loginModel,
            @RequestParam(value = "username", required = true) String userName,
            @RequestParam(value = "__token", required = true) String token,
            @RequestParam(value = "main_chart_name", required = true) String main_chart_name,
            @RequestParam(value = "second_chart_name", required = true) String second_chart_name,
            @RequestParam(value = "country_code", required = false) String country_code,
            @RequestParam(value = "region_code", required = false) String region_code,
            @RequestParam(value = "sex", required = false) String sex,
            @RequestParam(value = "year", required = false) int[] year
            ) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        JSONObject jo = new JSONObject();
        HttpStatus status = HttpStatus.OK;
        HashMap<String, String> tokenAuthRes = new TokenAuthDao(userName, token).checkLogin();
        if (tokenAuthRes.get("status").equals(LoginStatus.SUCCESS.toString())){
            DataGetter dg = new DataGetter(main_chart_name, second_chart_name);
            if (country_code != null && !country_code.isEmpty())
                dg.addClause(StatisticClauseFields.COUNTRY, country_code);
            if (region_code != null && !region_code.isEmpty()) dg.addClause(StatisticClauseFields.REGION, region_code);
            if (sex != null && !sex.isEmpty()) dg.addClause(StatisticClauseFields.SEX, sex);
            if (year != null && year.length > 0) dg.addClause(StatisticClauseFields.YEAR, IntStream.of(year).boxed().collect(Collectors.toList()));
            jo = dg.getJsonRepr();
            jo.put("status", tokenAuthRes.get("status"));
            jo.put("reason", tokenAuthRes.get("reason"));
        } else {
            jo.put("status", tokenAuthRes.get("status"));
            jo.put("reason", tokenAuthRes.get("reason"));
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jo.toJSONString(), responseHeaders, status);
    }

    @RequestMapping(value = "/get_years", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> get_years(
            ModelMap loginModel,
            @RequestParam(value = "username", required = true) String userName,
            @RequestParam(value = "__token", required = true) String token){
        JSONObject jo = new JSONObject();
        HashMap<String, String> tokenAuthRes = new TokenAuthDao(userName, token).checkLogin();
        HttpStatus status = HttpStatus.OK;
        if (tokenAuthRes.get("status").equals(LoginStatus.SUCCESS.toString())){
            List<Integer> years = getYears();
            Collections.sort(years);
            jo.put("status", tokenAuthRes.get("status"));
            jo.put("years", years);
        } else {
            jo.put("status", tokenAuthRes.get("status"));
            jo.put("reason", tokenAuthRes.get("reason"));
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jo.toJSONString(), responseHeaders, status);
    }

    @RequestMapping(value = "/get_sexes", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> get_sexes(
            ModelMap loginModel,
            @RequestParam(value = "username", required = true) String userName,
            @RequestParam(value = "__token", required = true) String token,
            @RequestParam(value = "lang", required = false) String lang) throws UnsupportedEncodingException {
        JSONObject jo = new JSONObject();
        HashMap<String, String> tokenAuthRes = new TokenAuthDao(userName, token).checkLogin();
        HttpStatus status = HttpStatus.OK;
        if (tokenAuthRes.get("status").equals(LoginStatus.SUCCESS.toString())){
            if (lang == null) {
                lang = Languages.EN.getCode();
            }

            Languages lg = Languages.createFromCode(lang);

            List<Map<String, String>> sexes = getSexes(lg);
            jo.put("status", tokenAuthRes.get("status"));
            jo.put("sexes", sexes);
        } else {
            jo.put("status", tokenAuthRes.get("status"));
            jo.put("reason", tokenAuthRes.get("reason"));
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<>(jo.toJSONString(), responseHeaders, status);
    }

    @RequestMapping(value = "/get_regions", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> get_regions(
            ModelMap loginModel,
            @RequestParam(value = "username", required = true) String userName,
            @RequestParam(value = "__token", required = true) String token,
            @RequestParam(value = "lang", required = false) String lang) {
        JSONObject jo = new JSONObject();
        HashMap<String, String> tokenAuthRes = new TokenAuthDao(userName, token).checkLogin();
        HttpStatus status = HttpStatus.OK;
        if (tokenAuthRes.get("status").equals(LoginStatus.SUCCESS.toString())){
            if (lang == null) {
                lang = Languages.EN.getCode();
            }
            Languages lg = Languages.createFromCode(lang);
            List<Map<String, String>> regions = getRegions(lg);
            jo.put("status", tokenAuthRes.get("status"));
            jo.put("regions", regions);
        } else {
            jo.put("status", tokenAuthRes.get("status"));
            jo.put("reason", tokenAuthRes.get("reason"));
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<>(jo.toJSONString(), responseHeaders, status);
    }

    @RequestMapping(value = "/get_countries", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> get_countries(
            ModelMap loginModel,
            @RequestParam(value = "username", required = true) String userName,
            @RequestParam(value = "__token", required = true) String token,
            @RequestParam(value = "region", required = true) String region,
            @RequestParam(value = "lang", required = false) String lang) {
        JSONObject jo = new JSONObject();
        HashMap<String, String> tokenAuthRes = new TokenAuthDao(userName, token).checkLogin();
        HttpStatus status = HttpStatus.OK;
        if (tokenAuthRes.get("status").equals(LoginStatus.SUCCESS.toString())){
            if (lang == null) {
                lang = Languages.EN.getCode();
            }
            Languages lg = Languages.createFromCode(lang);
            List<Map<String, String>> countries = getCountries(lg, region);
            jo.put("status", tokenAuthRes.get("status"));
            jo.put("regions", countries);
        } else {
            jo.put("status", tokenAuthRes.get("status"));
            jo.put("reason", tokenAuthRes.get("reason"));
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<>(jo.toJSONString(), responseHeaders, status);
    }

    @RequestMapping(value = "/get_statistics", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> get_statistics(
            ModelMap loginModel,
            @RequestParam(value = "username", required = true) String userName,
            @RequestParam(value = "__token", required = true) String token,
            @RequestParam(value = "lang", required = false) String lang) {
        JSONObject jo = new JSONObject();
        HashMap<String, String> tokenAuthRes = new TokenAuthDao(userName, token).checkLogin();
        HttpStatus status = HttpStatus.OK;
        if (tokenAuthRes.get("status").equals(LoginStatus.SUCCESS.toString())) {
            if (lang == null) {
                lang = Languages.EN.getCode();
            }
            Languages lg = Languages.createFromCode(lang);
            List<Map<String, String>> statistics = getStatistics(lg);
            ArrayList<HashMap<String, String>> stats = new ArrayList<>();
            statistics.forEach(statistic -> {
                        HashMap<String, String> ff = new HashMap<>();
                        statistic.keySet().forEach(
                                key -> ff.put(key, statistic.get(key))
                        );
                        stats.add(ff);
                    }
            );
            jo.put("status", tokenAuthRes.get("status"));
            jo.put("statistics", stats);
        } else {
            jo.put("status", tokenAuthRes.get("status"));
            jo.put("reason", tokenAuthRes.get("reason"));
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<>(jo.toJSONString(), responseHeaders, status);
    }

    @RequestMapping(value = "/contains_sex", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> contains_sex(
            ModelMap loginModel,
            @RequestParam(value = "username", required = true) String userName,
            @RequestParam(value = "__token", required = true) String token,
            @RequestParam(value = "main_chart", required = true) String main_chart_name,
            @RequestParam(value = "second_chart", required = true) String second_chart_name) {
        JSONObject jo = new JSONObject();
        HashMap<String, String> tokenAuthRes = new TokenAuthDao(userName, token).checkLogin();
        HttpStatus status = HttpStatus.OK;
        if (tokenAuthRes.get("status").equals(LoginStatus.SUCCESS.toString())) {
            jo.put("status", tokenAuthRes.get("status"));
            jo.put("contains_sex", containsSex(main_chart_name) && containsSex(second_chart_name));
        } else {
            jo.put("status", tokenAuthRes.get("status"));
            jo.put("reason", tokenAuthRes.get("reason"));
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(jo.toJSONString(), responseHeaders, status);
    }

    @RequestMapping(value = "/get_languages", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> get_languages() {
        HttpStatus status = HttpStatus.OK;
        JSONObject jo = new JSONObject();
        Languages[] langs = Languages.values();
        for (Languages lang : langs) {
            jo.put(lang.getCode(), lang.getStringName());
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<>(jo.toJSONString(), responseHeaders, status);
    }

    @RequestMapping(value = "/get_login_page_labels", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> get_login_page_labels(@RequestParam(value = "lang", required = true) String lang) {
        HttpStatus status = HttpStatus.OK;
        JSONObject jo = new JSONObject();
        if (lang == null) {
            lang = Languages.EN.getCode();
        }
        Languages lg = Languages.createFromCode(lang);
        Map<String, String> labels = NecessaryDataHelper.getLoginPageLabels(lg);
        labels.forEach(jo::put);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<>(jo.toJSONString(), responseHeaders, status);
    }

    @RequestMapping(value = "/get_workspace_labels", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> get_workspace_labels(ModelMap loginModel,
                                                       @RequestParam(value = "username", required = true) String userName,
                                                       @RequestParam(value = "__token", required = true) String token,
                                                       @RequestParam(value = "lang", required = true) String lang) {
        JSONObject jo = new JSONObject();
        HashMap<String, String> tokenAuthRes = new TokenAuthDao(userName, token).checkLogin();
        HttpStatus status = HttpStatus.OK;
        HttpHeaders responseHeaders = new HttpHeaders();
        if (tokenAuthRes.get("status").equals(LoginStatus.SUCCESS.toString())) {
            Languages lg = Languages.createFromCode(lang);
            Map<String, String> labels = NecessaryDataHelper.getWorkSpaceLabels(lg);
            labels.forEach(jo::put);
            responseHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
            responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        } else {
            jo.put("status", tokenAuthRes.get("status"));
            jo.put("reason", tokenAuthRes.get("reason"));
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(jo.toJSONString(), responseHeaders, status);
    }

}
