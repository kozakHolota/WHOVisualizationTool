<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="core_helper_css.jsp" %>

#container{
background-color: var(--panel_background);
width: 556px;
height: 286px;
position:absolute;
top:50%;
left:50%;
padding:15px;
-ms-transform: translateX(-50%) translateY(-50%);
-webkit-transform: translate(-50%,-50%);
transform: translate(-50%,-50%);
border-radius: 25px;
text-align: center;
-webkit-align-items: center; /* Safari 7.0+ */
align-items: center;
display: inline-block;
justify-content:center;
}

form {
display: inline-block;
vertical-align: middle;
align-items: center;
text-align: center;
-webkit-align-items: center; /* Safari 7.0+ */
margin-bottom: 10px;
}

.labeled {
background-color: var(--input_bg_color);
width: 300px;
color: var(--input_border_color);
font-weight: bold;
border: 2px solid var(--input_border_color);
display: table;
}

.labeled_select {
width: 300px;
-webkit-appearance: none;
-moz-appearance: none;
appearance: none;       /* remove default arrow */
background: var(--input_bg_color) url("${pageContext.request.contextPath}/resources/img/down_arrow_select.jpg") no-repeat right;
color: var(--input_border_color);
font-weight: bold;
border: 2px solid var(--input_border_color);
display: table;
}

label {
    width: 300px;
    margin-bottom: 20px;
    display: inline-block;
}

#submit {
    position: absolute;
    right: 10%;
    bottom: 20%;
    width: 150px;
    background-color: var(--button_positive_bg_color);
    color: var(--button_positive_color);
    font-weight: bold;
    border: 2px solid var(--input_border_color);
}

#submit:hover {
    box-shadow: 0 12px 16px 0 rgba(0,0,0,0.24), 0 17px 50px 0 rgba(0,0,0,0.19);
}

#submit:active {
opacity: 0.6;
}

#reset {
    position: absolute;
    width: 150px;
    left: 10%;
    bottom: 20%;
    background-color: var(--button_negative_bg_color);
    color: var(--input_border_color);
    font-weight: bold;
    border: 2px solid var(--input_border_color);
}

#reset:hover {
    box-shadow: 0 12px 16px 0 rgba(0,0,0,0.24), 0 17px 50px 0 rgba(0,0,0,0.19);
}

#reset:active {
opacity: 0.6;
}

#flag { display: inline-block; }