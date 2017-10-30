<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

:root {
  --panel_background: #CCCCCC;
  --button_positive_bg_color: #33CC00;
  --button_negative_bg_color: #EDEDED;
  --button_positive_color: #FFFF00;
  --input_border_color: #3D80CC;
  --input_bg_color: #FFFF99;
  --footer_bg_color: #FFCC99;
  --footer_color: #000000;
  --statistic_result_table_color: #00CCCC;
}

body {
margin: 0px 0px 0px 0px;
}

input[type='text'], input[type='password'] {
background-color: var(--input_bg_color);
color: var(--input_border_color);
font-weight: bold;
border: 2px solid var(--input_border_color);
display: table;
}

select {
-webkit-appearance: none;
-moz-appearance: none;
appearance: none;       /* remove default arrow */
background: var(--input_bg_color) url("${pageContext.request.contextPath}/resources/img/down_arrow_select.jpg") no-repeat right;
color: var(--input_border_color);
font-weight: bold;
border: 2px solid var(--input_border_color);
display: table;
}

option { margin-right: 35px; }

footer {
            background-color: var(--footer_bg_color);
            color: var(--footer_color);
            width: 100%;
            bottom: 0;
            font-size: 9px;
            padding-right: 10px;
            margin-right: 10px;
            position: fixed;
            align-items: right;
            text-align: right;
            -webkit-align-items: right; /* Safari 7.0+ */
            z-index: 5;
}


.active_button {
    background-color: var(--button_positive_bg_color);
    color: var(--button_positive_color);
    font-weight: bold;
    border: 2px solid var(--input_border_color);
}

.active_button:hover {
    box-shadow: 0 12px 16px 0 rgba(0,0,0,0.24), 0 17px 50px 0 rgba(0,0,0,0.19);
}

.active_button:active {
opacity: 0.6;
}

.passive_button {
    background-color: var(--button_negative_bg_color);
    color: var(--input_border_color);
    font-weight: bold;
    border: 2px solid var(--input_border_color);
}

.passive_button:hover {
    box-shadow: 0 12px 16px 0 rgba(0,0,0,0.24), 0 17px 50px 0 rgba(0,0,0,0.19);
}

.passive_button:active {
opacity: 0.6;
}