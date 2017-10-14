<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="helper_core_js.jsp" %>
var lang = "${lang}";
var userName = "${username}";
var token = "${token}";

function get_countries(userName, token, region_code){
    var countries_url = "/webapp/rest/get_countries";
    $.ajax(
        {
            url: countries_url,
           type: "POST",
           dataType: "json",
           data: {
               username: userName,
               __token: token,
               region: region_code,
               lang: lang
           }
        })
    .done(function (data) {
        var parsed_data = $.parseJSON(JSON.stringify(data));
        $('#country_code').empty();
        $('#country_code').append(create_option('', 'Всі країни'));
        $.each(parsed_data.regions, function(index, country_code){
            $('#country_code').append(create_option(country_code.country, country_code.country_localized_value));
        });
    });
}

function get_statistics(userName, token, selected_chart_id, refresh_main_chart){
var statistics_url = "/webapp/rest/get_statistics";
    $.ajax(
        {
           url: statistics_url,
           type: "POST",
           dataType: "json",
           data: {
               username: userName,
               __token: token,
               lang: lang
           }
        })
    .done(function (data) {
        var parsed_data = $.parseJSON(JSON.stringify(data));
        var menus = ['main_chart_name', 'second_chart_name'];

        if (refresh_main_chart == true) {
        $('#' + selected_chart_id).empty();
        $.each(parsed_data.statistics, function(index, statistic){
             $('#' + selected_chart_id).append(create_option(statistic['table_name'], statistic['localized_value']));
        });
        var main_index = menus.indexOf(selected_chart_id);
        if (main_index == -1) {
        alert("Помилковий ідентифікатор меню статистики");
        return false;
        }
        }

        menus.splice(main_index, 1);
        $('#' + menus[0]).empty();

        $.each(parsed_data.statistics, function(index, statistic){
                    if(statistic['table_name'] != $('#' + selected_chart_id).val()) $('#' + menus[0]).append(create_option(statistic['table_name'], statistic['localized_value']));
                });
    });
}

function get_all_statistics(userName, token) {get_statistics(userName, token, 'main_chart_name', true);}

function get_sexes(userName, token){
var sexes_url = "/webapp/rest/get_sexes";
var contains_sex_url = "/webapp/rest/contains_sex";
$.ajax({
                     url: sexes_url,
                     type: "POST",
                     dataType: "json",
                     data: {
                         username: userName,
                        __token: token,
                        lang: lang
                        }
                 })
                 .done(function (data) {
                     var parsed_data = $.parseJSON(JSON.stringify(data));
                     $('#sex').empty();
                     $('#sex').append(create_option('', 'Не вказувати'));
                     var contains_sex = false;
                     $.ajax(
                     {
                                          url: contains_sex_url,
                                          type: "POST",
                                          dataType: "json",
                                          data: {
                                              username: userName,
                                             __token: token,
                                             main_chart: $('#main_chart_name').val(),
                                             second_chart: $('#second_chart_name').val()
                                             }
                                      }
                     ).done(function (data) {
                     var parsed_data = $.parseJSON(JSON.stringify(data));
                     contains_sex = (parsed_data.contains_sex === "true");
                     console.log("Does query contains sex option? " + contains_sex);
                     });

                     if(contains_sex == true){
                     $.each(parsed_data.sexes, function(index, sex){
                     $('#sex').append(create_option(sex['sex'], sex['sex_localized_value']));
                     });
                     }
                 });
}

function get_regions(){
var regions_url = "/webapp/rest/get_regions";
$.ajax({
                url: regions_url,
                type: "POST",
                dataType: "json",
                data: {
                    username: userName,
                   __token: token,
                   lang: lang
                   }
            })
            .done(function (data) {
                var parsed_data = $.parseJSON(JSON.stringify(data));
                $('#region_code').empty();
                $('#region_code').append(create_option('', 'Всі географічні регіони'));
                $.each(parsed_data.regions, function(index, region_code){
                $('#region_code').append(create_option(region_code['region'], region_code['region_localized_value']));
                });
            });
}

function get_years(){
var years_url = "/webapp/rest/get_years";
$.ajax({
                            url: years_url,
                            type: "POST",
                            dataType: "json",
                            data: {
                                username: userName,
                               __token: token
                               }
                        })
                        .done(function (data) {
                            var parsed_data = $.parseJSON(JSON.stringify(data));
                            $('#from_year').empty();
                            $('#to_year').empty();
                            $.each(parsed_data.years, function(index, year){
                            $('#from_year').append(create_option(year, year));
                            $('#to_year').append(create_option(year, year));
                            });
     });
}

function display_statistics(){
        var myUrl = "/webapp/rest/get_chart";
        var main_chart_name = $('#main_chart_name').val();
        var second_chart_name = $('#second_chart_name').val();
        var region_code = $('#region_code').val();
        var country_code = $('#country_code').val();
        var years = []
        years.push($('#from_year').val());
        years.push($('#to_year').val());
        var sex = $('#sex').val();
        $.ajax({
            url: myUrl,
            type: "POST",
            dataType: "json",
            traditional: true,
            data: {
                username: userName,
               __token: token,
               main_chart_name: main_chart_name,
               second_chart_name: second_chart_name,
               region_code: region_code,
               country_code: country_code,
               sex: sex,
               year: years
               }
        })
        .done(function (data) {
            var parsed_data = $.parseJSON(JSON.stringify(data));
            var y_vals = [];
            $.merge(y_vals, parsed_data.main_chart_points);
            $.merge(y_vals, parsed_data.second_chart_points);
            $.unique(y_vals);
            $("#statistic").html("<canvas id='statistical_graph' height='720' width='720'></canvas>");
            $("#x_axis_div").html("<canvas id='x_axis_labels' height='15' width='720'></canvas>");
            var x_axis = 0;
            var canvas = $("#statistical_graph").get(0);
            var lbl_canvas = $("#x_axis_labels").get(0);
            var context = canvas.getContext('2d');
            var lbl_context = lbl_canvas.getContext('2d');

            lbl_context.lineWidth = 2;
            lbl_context.fillStyle = "black";
            lbl_context.beginPath();

            context.lineWidth = 2;
            context.beginPath();
            context.translate(0,720);
            context.scale(1,-1);

            context.beginPath();

            $.each(parsed_data.years, function(index, item) {
            if(index == 0) {context.moveTo(x_axis, parsed_data.main_chart_points[index] + 15);} else {
                  context.lineTo(x_axis, parsed_data.main_chart_points[index] + 15);
                  }

                  x_axis += 50;
            });
            context.strokeStyle = "red";
            context.stroke();

            x_axis = 0;
            context.beginPath();
            $.each(parsed_data.years, function(index, item) {
                        if(index == 0) {context.moveTo(x_axis, parsed_data.second_chart_points[index] + 15);} else {
                              context.lineTo(x_axis, parsed_data.second_chart_points[index] + 15);
                              }

                              x_axis += 50;
                        });
            context.strokeStyle = "green";
            context.stroke();


            x_axis = 0;
            context.beginPath();
            lbl_context.beginPath();
            $.each(parsed_data.years, function(index, item) {
                                    if(index == 0) {
                                    context.moveTo(x_axis, 15);
                                    }
                                    else {
                                    context.lineTo(x_axis, 15);
                                    context.lineTo(x_axis, 11);
                                    context.moveTo(x_axis, 15);
                                    lbl_context.font = "10px Arial";
                                    lbl_context.fillText(item, x_axis - 7, 10);
                                    }
                                    x_axis += 50;
                                    });


            context.lineTo(x_axis + 50, 15);
            context.lineTo(x_axis + 45, 19);
            context.moveTo(x_axis + 50, 15);
            context.lineTo(x_axis + 45, 11);
            context.strokeStyle = "black";
            context.stroke();

            context.beginPath();
            var cur_y = 0;
            context.moveTo(0, 15);
            $.each(y_vals, function(index, item) {
            cur_y = item + 15;
            context.lineTo(0, cur_y);
            });

            context.lineTo(0, cur_y + 30);
            context.strokeStyle = "black";
            context.lineWidth = 2;
            context.stroke();

            canvas.addEventListener('mousemove', function(event){
            var rect = canvas.getBoundingClientRect();
            var x = event.layerX - rect.right;
            var y = event.layerY - rect.bottom;
            context.font = "10px Arial";
            context.fillText(y - 15, x, y + 5);
            });

            var html = "";
            html += "<table id='statistic'><tr><th>Рік</th><th>" + $('#main_chart_name').find(':selected').text() + "</th><th>" + $('#second_chart_name').find(':selected').text() + "</th></tr>";
            $.each(parsed_data.years, function(i, item) {
                var row = "<tr><td>" + parsed_data.years[i] + "</td><td>" + parsed_data.main_chart_points[i] + "</td><td>"+ parsed_data.second_chart_points[i] + "</td></tr>";
                html += row;
            });

            html += "<tr><td>Кореляція</td><td colspan=2>" + parsed_data.correlation_coefficient + "</td></tr>";

            html += + "</table>";
            $('#statistical_data').html(html);

        });
}