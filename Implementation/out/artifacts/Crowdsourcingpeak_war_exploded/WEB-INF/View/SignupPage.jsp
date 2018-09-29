<%@ page import="org.json.JSONObject" %><%--
  Created by IntelliJ IDEA.
  Manager: Francesco Alongi
  Date: 31/05/2018
  Time: 11:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>Signup</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Stylesheet/backgroundMountains.css">
</head>
<body>

<div class="container">
    <div class="row">
        <div class="col-md-4 col-md-offset-4 text-center">
            <h1>CrowdsourcingPeak Project</h1>
            <h3>Signup</h3>
            <br>

            <% if (request.getAttribute("errorInformation") != null) { %>

            <div class="alert alert-warning">
                <%=request.getAttribute("errorInformation")%>
            </div>

            <%}%>

            <form method="post" action="${pageContext.request.contextPath}/SignupCheck" id ="signupForm">
                <label for="username">Username:</label>
                <div class="form-group">
                    <input class="form-control" type="text" name="username" id="username" placeholder="Enter username">
                </div>
                <label for="password">Password:</label>
                <div class="form-group">
                    <input class="form-control" type="password" name="password" id="password" placeholder="Enter password">
                </div>
                <label for="passwordConfirm">Confirm your password:</label>
                <div class="form-group">
                    <input class="form-control" type="password" name="passwordConfirm" id="passwordConfirm" placeholder="Enter again password">
                </div>
                <label for="email">Email:</label>
                <div class="form-group">
                    <input class="form-control" type="text" name="email" id="email" placeholder="Enter email">
                </div>
                <div class="form-group form-check">
                    <label class="form-check-label">
                        <input class="form-check-input" type="checkbox" name="mngCheckBox" id="mngCheckBox" value="manager"> I'm a manager
                    </label>
                </div>
                <input type="submit" value="Submit" class="btn btn-primary">
            </form>
        </div>
    </div>
</div>
</body>
</html>
