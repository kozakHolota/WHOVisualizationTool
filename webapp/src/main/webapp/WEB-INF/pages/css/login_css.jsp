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

label {
    width: 300px;
    margin-bottom: 20px;
    display: inline-block;
}

#flag { display: inline-block; }

#submit {
position: absolute;
right: 10%;
bottom: 20%;
width: 150px;
}

#reset {
position: absolute;
    width: 150px;
    left: 10%;
    bottom: 20%;
}

.labeled { width: 300px; }