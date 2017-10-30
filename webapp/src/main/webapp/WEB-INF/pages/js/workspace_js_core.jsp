<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="helper_js.jsp" %>
<%@include file="workspace_helper_js.jsp" %>
function display_menu(){
if(!menu_displayed) {
$("#additional_side").css("display", "block");
menu_displayed = true;
}
else {
$("#additional_side").css("display", "none");
menu_displayed = false;
}
}

function exit_start_menu() {
$("#additional_side").css("display", "none");
menu_displayed = false;
}

$(document).ready(function() {
     set_all();
     canvas = $("#statistical_graph").get(0);
     ctx = canvas.getContext('2d');
     ctx.save();

     legend_canvas = $("#statstical_legend").get(0);
     legend_ctx = legend_canvas.getContext('2d');
     legend_ctx.save();

     $('#language').change(function(){set_all($(this).val())});

     $('#region_code').change(function(){get_countries(userName, token, $('#region_code').val())});

     $('#main_chart_name').change(
     function(){get_statistics(userName, token, 'main_chart_name', false);
                     get_sexes(userName, token);
                     });

     $("#get_chart").click(function(event){
     display_statistics();
     }
     );

     $("#start_menu").click(function(event){
            display_menu();
         }
      );

      $("#logout").click(function(event){
                             logout();
                          }
                       );
}
);
