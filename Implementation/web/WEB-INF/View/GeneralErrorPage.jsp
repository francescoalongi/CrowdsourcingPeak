<%--
  Created by IntelliJ IDEA.
  User: Francesco Alongi
  Date: 04/09/2018
  Time: 10:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body>
<nav class="navbar navbar-default" style="background-color: #e3f2fd;">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="${pageContext.request.contextPath}">CrowdsourcingPeak</a>
        </div>
    </div>
</nav>

<div class="container">
    <div class="row">
        <div class="col">
            <h1>Oops! Something went wrong...</h1>
            <h3>Seems that you don't have the permission to get here.</h3>
            Click <a href="${pageContext.request.contextPath}/">here</a> and let's start again from the beginning!
        </div>
    </div>
</div>
</body>
</html>
