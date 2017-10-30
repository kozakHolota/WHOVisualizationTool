<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="helper_js.jsp" %>
function set_labels(lang) {
var labels_url = "/webapp/rest/get_login_page_labels";
    $.ajax(
        {
            url: labels_url,
           type: "GET",
           dataType: "json",
           data: "lang=" + lang
        })
    .done(function (data) {
        var parsed_data = $.parseJSON(JSON.stringify(data));
        $('title').empty();
        $('title').append(parsed_data['MW_MAIN_TITLE'] + ' - ' + parsed_data['LP_SUBTITLE']);
        $('#username_label').empty();
        $('#username_label').append(parsed_data['LP_USERNAME_LABEL'] + '<br>');
        $('#username_label').append('<input type="text" name="username" id="username" class="labeled"/>');
        $('#password_label').empty();
        $('#password_label').append(parsed_data['LP_PASSWORD_LABEL'] + '<br>');
        $('#password_label').append('<input type="password" name="password" id="password" class="labeled"/>');
        $('#submit').prop('value', parsed_data['LP_LOGIN_BUTTON_LABEL']);
        $('#reset').prop('value', parsed_data['LP_RESET_BUTTON_LABEL']);
        set_footer(parsed_data);
    });
}