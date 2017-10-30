<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="helper_core_js.jsp" %>

function set_langs_menu(dropdown_id){
var countries_url = "/webapp/rest/get_languages";
$.ajax(
        {
            url: countries_url,
           type: "GET",
           dataType: "json"
        })
    .done(function (data) {
        var parsed_data = $.parseJSON(JSON.stringify(data));
         $('#' + dropdown_id).empty();
        $.each(parsed_data, function(language_code, language_name){
            $('#' + dropdown_id).append(create_option(language_code, language_name));
        });
    });
}