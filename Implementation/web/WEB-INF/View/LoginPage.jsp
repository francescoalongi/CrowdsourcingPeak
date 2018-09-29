<%@ page import="org.json.JSONObject" %><%--
  Created by IntelliJ IDEA.
  Manager: Francesco Alongi
  Date: 31/05/2018
  Time: 10:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Stylesheet/backgroundMountains.css">
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-4 col-md-offset-4 text-center">
            <h1>CrowdsourcingPeak Project</h1>
            <h3>Login</h3>
            <br>
            <% if (request.getAttribute("errorInformation") != null) { %>

            <div class="alert alert-warning">
                    <%=request.getAttribute("errorInformation")%>
            </div>
            <% } %>

            <form method ="POST" action="${pageContext.request.contextPath}/LoginCheck" id ="loginForm">
                <label for="username">Username:</label>
                <div class="form-group">
                    <input class="form-control" type="text" name="username" id="username" placeholder="Enter username">
                </div>
                <label for="password">Password:</label>
                <div class="form-group">
                    <input type="password" class="form-control" name="password" id="password" placeholder="Enter password">
                </div>
                <input type="submit" value="Submit" class="btn btn-primary">
            </form>
            <br>
            Click <a href="${pageContext.request.contextPath}/Signup">here</a> if you don't have an account yet.
        </div>
    </div>
</div>
</body>
</html>

