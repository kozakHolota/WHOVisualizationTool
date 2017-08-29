<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<title>Workspace</title>
<script type="text/javascript"
src="/webapp/jquery" />"></script>
<script type="text/javascript" language="javascript">
$(document).ready(function() {
    $("#get_chart").click(function(event){
        var myUrl = "/webapp/rest/get_chart";
        var userName = $('input[name=username]').val();
        var token = $('input[name=__token]').val();
        var main_chart_name = $('input[name=main_chart_name]').val();
        var second_chart_name = $('input[name=second_chart_name]').val();
        var region_code = $('input[name=region_code]').val();
        var years = []
        $('input[name=year]').each(function(){
        years.push($(this).val());
        });

        $.ajax({
            url: myUrl,
            type: "POST",
            dataType: "json",
            data: {
                username: userName,
               __token: token,
               main_chart_name: main_chart_name,
               second_chart_name: second_chart_name,
               region_code: region_code,
               year: years
               }
        })
        .done(function (data) {
            var parsed_data = $.parseJSON(JSON.stringify(data));
            var html = "<h2>Кореляція між споживанням табака підлітками 13-15 років та очікуваною середньою тривалістю життя у Європі у період 1990-2015 роках</h2><br>"
            html += "<table id='statistic'><tr><th>Рік</th><th>Споживання табаку серед підлітків</th><th>Очікувана тривалість життя</th></tr>";
            $.each(parsed_data.years, function(i, item) {
                var row = "<tr><td>" + parsed_data.years[i] + "</td><td>" + parsed_data.main_chart_points[i] + "</td><td>"+ parsed_data.second_chart_points[i] + "</td></tr>";
                html += row;
            });

            html += "<tr><td>Кореляція</td><td colspan=2>" + parsed_data.correlation_coefficient + "</td></tr>"

            html += + "</table>";
            $('#statistical_data').html(html);
        });
    }
    );
}
);
</script>
</head>
<body>
<h2>Workspace</h2>
<input type="hidden" name="username" value="${username}">
<input type="hidden" name="__token" value="${token}">
<input type="hidden" name="main_chart_name" value="tobacco_usage">
<input type="hidden" name="second_chart_name" value="life_expectancy">
<input type="hidden" name="region_code" value="Europe">
<input type="hidden" name="year[]" value='["1990", "2015"]'>
<br>
<input type="button" id="get_chart" value="Видати статистику =>"><br>
<div id="statistical_data"></div>
</body>
</html>