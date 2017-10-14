<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="workspace_helper_js.jsp" %>

$(document).ready(function() {
     get_regions();
     get_years();

     get_countries(userName, token, $('#region_code').val());
     get_all_statistics(userName, token);
     get_sexes(userName, token);

     $('#region_code').change(function(){get_countries(userName, token, $('#region_code').val())});

     $('#main_chart_name').change(
     function(){get_statistics(userName, token, 'main_chart_name', false);
                     get_sexes(userName, token);
                     });

     $("#get_chart").click(function(event){
     display_statistics();
     }
     );
}
);
