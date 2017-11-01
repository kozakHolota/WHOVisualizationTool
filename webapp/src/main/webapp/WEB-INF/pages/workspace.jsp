<!DOCTYPE html>
<html>
<head>
<meta http-equiv="x-ua-compatible" content="ie=edge">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<title></title>
<style type="text/css">
<%@include file="css/workspace_css.jsp" %>
</style>
<script type="text/javascript" src="/webapp/jquery"></script>
<script type="text/javascript" language="javascript">
<%@include file="js/workspace_js_core.jsp" %>
</script>
</head>
<body>
<div id="main">
<nav>
<div id="nav_control_box" style="left: 5px;">
<div class="config_box">
<select id="language" class="mn"></select> &nbsp;
<div id="flag"></div>
</div>
<div class="config_box" style="right: 5px;">
<button id="logout" class="logout_button">
<img src="${pageContext.request.contextPath}/resources/img/logout.jpg" class="logout_icon"/>
</button>
</div>
</div>
</nav>
<div id="initial_side">
<button id="start_menu">
<img src="${pageContext.request.contextPath}/resources/img/munu_button.png"/>
</button>
</div>
<div id="additional_side">
<input type="button" id="get_chart" class="active_button"><br><br>
<fieldset>
<legend id="chart_legend"></legend>
<label id="first_chart_label"></label>
<select id="main_chart_name" class="dim_select"></select><br>
<label id="second_chart_label"></label>
<select id="second_chart_name" class="dim_select"></select>
</fieldset>
<fieldset>
<legend id="geo_dim_legend"></legend>
<label id="geo_region"></label>
<select id="region_code" class="dim_select"></select><br>
<label id="country"></label>
<select id="country_code" class="dim_select">
</select>
</fieldset>
<fieldset>
<legend id="sex_label"></legend>
<select id="sex" class="dim_select"></select>
</fieldset>
<fieldset>
<legend id="years_legend"></legend>
<select id="from_year" class="from"></select>
<select id="to_year" class="to"></select>
</fieldset>
</div>
<div id="main_content">
<div id="statistical_box">
<div id="graph_box">
<canvas id='statistical_graph' width=700 height=400></canvas><br>
<canvas id='statstical_legend' width=700 height=70></canvas>
</div>
<div id="statistical_data"></div>
</div>
</div>
<div id="start_article">
<p>
Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.<br>
Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.<br>
Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.<br>
Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?<br>
</p>
<p>
Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?<br>
At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga.<br>
Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere
</p>
</div>
<%@include file="page_parts/footer.jsp" %>
</div>
</body>
</html>