<!DOCTYPE html>
<html>
<head>
<meta http-equiv="x-ua-compatible" content="ie=edge">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<title>Робочий Кабінет</title>
<style type="text/css">
<%@include file="css/workspace_css.jsp" %>
</style>
<script type="text/javascript" src="/webapp/jquery"></script>
<script type="text/javascript" language="javascript">
<%@include file="js/workspace_js_core.jsp" %>
</script>
</head>
<body>
<h2>Робочий Кабінет</h2><br><br>
<b>Статистика першого ряду даних</b>&nbsp;
<select id="main_chart_name"></select><br><br>
<b>Статистика другого ряду даних</b>&nbsp;
<select id="second_chart_name"></select><br><br>
<b>Географічний регіон</b>&nbsp;
<select id="region_code"></select><br><br>
<b>Країна</b>&nbsp;
<select id="country_code">
</select><br><br>
<b>Стать</b>&nbsp;
<select id="sex"></select>
<br><br>
<b>З року&nbsp;</b>
<select id="from_year"></select>
<b>По рік&nbsp;</b>
<select id="to_year"></select>
<br><br><br>
<input type="button" id="get_chart" value="Видати статистику =>"><br><br>
<div id="statistic"></div><br>
<div id="x_axis_div"></div><br>
<div id="statistical_data"></div><br><br>
</body>
</html>