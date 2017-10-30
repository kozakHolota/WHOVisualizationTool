<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="helper_core_js.jsp" %>
<%@include file="canvas_chart_js.jsp" %>
var lang = "${lang}";
var userName = "${username}";
var token = "${token}";
var correlation_label = "";
var stat_losung = "";
var stat_title = "";
var stat_legend_title = "";
var main_stat_title, second_stat_title;
var year = "";
var menu_displayed = false;
var canvas, ctx;
var geo_region_label, country_label, sex_label;

function logout()
{
  var form = $('<form></form>');
  $(form).hide().attr('method','post').attr('action',"/webapp/logout");
  $(form).append("<input type='hidden' name='username' value='" + userName + "'/>");
  $(form).append("<input type='hidden' name='__token' value='" + token + "'/>");
  $(form).appendTo('body').submit();
}

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

function set_workspace_labels() {
var labels_url = "/webapp/rest/get_workspace_labels";
$.ajax({
                            url: labels_url,
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
                                year = parsed_data['WS_YEAR_LABEL'];
                                stat_legend_title = parsed_data['WS_LEGEND_TITLE_LABEL'];
                                stat_losung = parsed_data['WS_STAT_LOSUNG_LABEL'];
                                stat_title = parsed_data['WS_STAT_TITLE_LABEL'];
                                correlation_label = parsed_data['WS_CORRELATION_LABEL'];
                                first_stat_title = parsed_data['WS_FIRST_CHART_LABEL'];
                                second_stat_title = parsed_data['WS_SECOND_CHART_LABEL'];
                                geo_region_label = parsed_data['WS_REGION_LABEL'];
                                country_label = parsed_data['WS_COUNTRY_LABEL'];
                                sex_label = parsed_data['WS_SEX_LEGEND'];
                                $('title').empty();
                                $('title').append(parsed_data['MW_MAIN_TITLE'] + " - " + parsed_data['WS_SUBTITLE']);
                                $("#get_chart").empty();
                                $("#get_chart").val(parsed_data['WS_STATISTIC_BTN_LABEL']);
                                $("#chart_legend").empty();
                                $("#chart_legend").append(parsed_data['WS_CHART_LEGEND_LABEL']);
                                $("#first_chart_label").empty();
                                $("#first_chart_label").append(parsed_data['WS_FIRST_CHART_LABEL']);
                                $("#second_chart_label").empty();
                                $("#second_chart_label").append(parsed_data['WS_SECOND_CHART_LABEL']);
                                $("#geo_dim_legend").empty();
                                $("#geo_dim_legend").append(parsed_data['WS_GEO_DIM_LABEL']);
                                $("#geo_region").empty();
                                $("#geo_region").append(parsed_data['WS_REGION_LABEL']);
                                $("#country").empty();
                                $("#country").append(parsed_data['WS_COUNTRY_LABEL']);
                                $("#sex_label").empty();
                                $("#sex_label").append(parsed_data['WS_SEX_LEGEND']);
                                $("#years_legend").empty();
                                $("#years_legend").append(parsed_data['WS_YEARS_LEGEND']);
                                set_footer(parsed_data);
                        });
}

function set_all(language=lang) {
if (language != lang) { lang = language; }
set_langs_menu("language");
$('#lang').val(lang);
set_flag(lang);
set_workspace_labels();
get_regions();
get_years();
get_countries(userName, token, $('#region_code').val());
get_all_statistics(userName, token);
get_sexes(userName, token);
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
            CanvasChart.render(parsed_data.years, parsed_data.main_chart_points, parsed_data.second_chart_points);

            $("#statstical_legend").css("border", "2px solid black");
            legend_ctx.clearRect(0, 0, legend_canvas.width, legend_canvas.height);
            legend_ctx.restore();
            legend_ctx.save();
            legend_ctx.font = 'bold 12pt Arial';
            legend_ctx.textAlign = "center";
            legend_ctx.fillText(stat_legend_title, legend_canvas.width/2, 15);
            legend_ctx.strokeStyle = 'red';
            legend_ctx.beginPath();
            legend_ctx.moveTo(5, 30);
            legend_ctx.lineTo(135, 30);
            legend_ctx.stroke();
            legend_ctx.closePath();
            legend_ctx.font = '9pt Arial';
            legend_ctx.textAlign = "right";
            legend_ctx.fillText($('#main_chart_name').find(':selected').text(), 340, 30);
            legend_ctx.strokeStyle = 'green';
            legend_ctx.beginPath();
            legend_ctx.moveTo(5, 60);
            legend_ctx.lineTo(135, 60);
            legend_ctx.stroke();
            legend_ctx.closePath();
            legend_ctx.fillText($('#second_chart_name').find(':selected').text(), 340, 60);

            var html = "";
            html += "<table id='statistical_table'>"
            + "<thead>"
            + "<caption>"
            + geo_region_label + ": " + $('#region_code').find(':selected').text()
            + "<br>"
            + country_label + ": " + $('#country_code').find(':selected').text()
            + "<br>"
            + sex_label + ": " + $('#sex').find(':selected').text()
            + "<br>"
            + correlation_label + ": " + parsed_data.correlation_coefficient
            + "</caption><tr><th>"
            + year
            + "</th><th>"
            + $('#main_chart_name').find(':selected').text()
            + "</th><th>" + $('#second_chart_name').find(':selected').text()
            + "</th></tr><tr><th></th><th><a href='"
            + parsed_data.main_chart_urls[0]
            + "'>JSON</a> &nbsp; <a href='"
            + parsed_data.main_chart_urls[1]
            + "'>CSV</a></th><th><a href='"
            + parsed_data.second_chart_urls[0]
            + "'>JSON</a> &nbsp; <a href='"
            + parsed_data.second_chart_urls[1]
            + "'>CSV</a></th></tr>"
            + "</thead>"
            + "<tbody>";

            $.each(parsed_data.years, function(i, item) {
                var row = "<tr><td>" + parsed_data.years[i] + "</td><td>" + parsed_data.main_chart_points[i] + "</td><td>"+ parsed_data.second_chart_points[i] + "</td></tr>";
                html += row;
            });

            html += "</tbody>" + "</table>";
            $('#statistical_data').html(html);
        });
}