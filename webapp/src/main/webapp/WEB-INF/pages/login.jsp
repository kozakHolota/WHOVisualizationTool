<html>
<head>
<%@ page isELIgnored="false" %>
<title>Login Page</title>
</head>
<body>
<h2>Login Page</h2><br>
<form name="log" id="log" action="/webapp/login" method="POST">
Loign: <input type="text" name="username"><br>
Password: <input type="password" name="password"><br>
<input type="submit"> <input type="reset">
</form>
</body>
</html>