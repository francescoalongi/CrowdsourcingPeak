<%@ page import="Model.User" %><%--
  Created by IntelliJ IDEA.
  User: Francesco Alongi
  Date: 03/06/2018
  Time: 17:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit manager data</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body>
<jsp:include page="/WEB-INF/View/UpperToolbar.jsp"/>
<div class="container">
    <div class="row">
        <h1>In this page you can edit your data.</h1>
        <% if (request.getAttribute("errorInformation") != null) { %>

        <div class="alert alert-warning">
            <%=request.getAttribute("errorInformation")%>
        </div>

        <%}%>
        <div class="col-md-4">
            <form method="post" action="${pageContext.request.contextPath}/UpdateUserData" id ="signupForm">
                <label for="username">Username:</label>
                <div class="form-group">
                    <input class="form-control" type="text" name="username" id="username" placeholder="<%=((User)session.getAttribute("user")).getUsername()%>">
                </div>
                <label for="newPassword">New password:</label>
                <div class="form-group">
                    <input class="form-control" type="password" name="newPassword" id="newPassword" placeholder="Enter new password">
                </div>
                <label for="newPasswordConfirm">Confirm your new password:</label>
                <div class="form-group">
                    <input class="form-control" type="password" name="newPasswordConfirm" id="newPasswordConfirm" placeholder="Enter new password">
                </div>
                <label for="email">Email:</label>
                <div class="form-group">
                    <input class="form-control" type="text" name="email" id="email" placeholder="<%=((User)session.getAttribute("user")).getEmail()%>">
                </div>
                <input type="submit" value="Submit" class="btn btn-primary">
            </form>
        </div>
    </div>
</div>
</body>
</html>
