<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="core_helper_css.jsp" %>
nav {
position: fixed;
z-index: 10;
top: 0%;
width: 100%;
height: 50px;
background-color: #66FF99;
}

fieldset {
padding: 5px;
max-width:260px;
min-height: 30px;
}

legend {
color: black;
font-weight: bold;
font-family: sans;
font-size: 12px;
}

label {
      color: black;
      font-weight: bold;
      font-family: sans;
      font-size: 12px;
      }

#main {
width: 100%;
height: 100%;
display: -webkit-flex; /* Safari */
-webkit-flex-direction: column; /* Safari 6.1+ */
display: flex;
flex-direction: column;
}

#initial_side {
order: 1;
top: 50px;
position: fixed;
float:left;
width: 70px;
height: 100%;
display: block;
background-color: var(--panel_background);
z-index: 9;
}

#additional_side {
order: 2;
position: fixed;
top: 50px;
left: 70px;
position: absolute;
float:left;
width: 300px;
height: 100%;
display: none;
background-color: var(--panel_background);
z-index: 9;
}

#main_content {
order: 3;
top: 50px;
bottom: 50px;
z-index: -1;
overflow: hidden;
position: absolute;
left: 70px;
}

#start_menu {
left: 2px;
position: fixed;
background-color: transparent;
border: none
}

#start_menu:hover {
               box-shadow: 0 12px 16px 0 rgba(0,0,0,0.24), 0 17px 50px 0 rgba(0,0,0,0.19);
}

#start_menu:active {
            opacity: 0.6;
            box-shadow: none;
}

#get_chart {
position: absolute;
right: 20px;
top: 15px
}

.config_box {
background-color: transparent;
position: absolute;
display: -webkit-flex; /* Safari */
display: flex;
align-items: center;
-webkit-align-items: center; /* Safari 7.0+ */
-webkit-align-content: center;
align-content: center;
height: 50px;
}


.logout {
background-color: transparent;
border: 0px;
}

.logout:hover {
               box-shadow: 0 12px 16px 0 rgba(0,0,0,0.24), 0 17px 50px 0 rgba(0,0,0,0.19);
}

.logout:active {
            opacity: 0.6;
            box-shadow: none;
}

.logout_icon {
background-color: transparent;
width: 30px;
height: 30px;
text-align: center;
vertical-align: middle;
}

.dim_select {
width: 250px;
left: 25px;
}

.from {
position: relative;
float: left;
top: 0px;
left: 2px;
}

.to {
position: relative;
float: right;
top: 0px;
right: 2px;
}

#nav_control_box {
position: relative;
display: -webkit-flex; /* Safari */
display: flex;
height: 50px;
width: 250px;
float: right;
right: 20px;
}

#statistical_table caption {
background-color: var(--statistic_result_table_color);
color: white;
font-weight: bold;
font-family: sans;
font-size: 12px;
}

#statistical_table th {
background-color: var(--statistic_result_table_color);
color: white;
font-weight: bold;
font-family: sans;
font-size: 12px;
word-wrap: break-word;
width: 100px;
}

#statistical_table th a {
font-family: sans;
font-weight: bold;
text-decoration: none;
color: gold;
font-size: 12px;
}

#statistical_table th a:hover {
font-family: sans;
font-weight: bold;
text-decoration: underline;
color: gold;
font-size: 14px;
}

#statistical_table tbody { overflow-y: auto; }

#statistical_table td {
border: 2px solid var(--statistic_result_table_color);
color: black;
font-weight: bold;
font-family: sans;
font-size: 12px;
width: 100px;
}

#statistical_box {
  width: 1200px;
  height: 600px;
  position: fixed;
  display: none;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  -webkit-transform: translate(-50%, -50%);
  -moz-transform: translate(-50%, -50%);
  -o-transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
}

#start_article {
  width: 1200px;
  height: 600px;
  position: fixed;
  display: flex;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  -webkit-transform: translate(-50%, -50%);
  -moz-transform: translate(-50%, -50%);
  -o-transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
}

#graph_box {
overflow: auto;
position: absolute;
left: 75px;
top: 75px;
}

#statistical_data {
position: absolute;
overflow: auto;
width: 400px;
right: 5px;
top: 75px;
}