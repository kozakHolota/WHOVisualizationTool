<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

function create_option(value, text) {
var option = $("<option></option>");
option.val(value);
option.text(text);
return option;
}

function set_flag(language_code) {
$('#flag').empty();
$('#flag').append("<img style='width: 30px; height: 20px' src='${pageContext.request.contextPath}/resources/img/" + language_code + "_flag.png'/>");
}