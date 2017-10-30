<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ page isELIgnored="false" %>
<script type="text/javascript" src="/webapp/jquery"></script>
<style type="text/css">
<%@include file="css/login_css.jsp" %>
</style>
<script type="text/javascript" language="javascript">
<%@include file="js/login_page_helper_js.jsp" %>
$(document).ready(function() {
var lang = "uk";
set_langs_menu("lang");
set_flag(lang);
set_labels(lang);
$('#lang').change(function() {
set_flag($(this).val());
set_labels($(this).val());
});

$('#log').submit(function(event){
$('.labeled').each(function(){
if($(this).val() == "") {
$(this).css("border", "2px solid red");
$(this).focus();
event.preventDefault();
return false;
}
});
});
});
</script>
<title></title>
</head>
<body>
<div id="container">
<form name="log" id="log" action="/webapp/login" method="POST">
<label id="username_label">
</label>
<label id="password_label">
</label>
<label><select id="lang" name="lang" class="labeled"></select><br><div id="flag"></div></label><br>
<input type="reset" id="reset" class="passive_button"><input type="submit" id="submit" class="active_button">
</form>
</div>
<%@include file="page_parts/footer.jsp" %>
</body>
</html>