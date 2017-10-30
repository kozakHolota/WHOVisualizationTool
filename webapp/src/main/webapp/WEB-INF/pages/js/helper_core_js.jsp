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

function set_footer(parsed_data){
        $('#author').empty();
        $('#author').append(parsed_data['FP_AUTHOR_SIGN']);
        $('#contact_info').empty();
        $('#contact_info').append(parsed_data['FP_AUTHOR_CONTACT'] + ': <a href="mailto:kozak_holota@gmail.com"> kozak_holota@gmail.com</a>.');
}