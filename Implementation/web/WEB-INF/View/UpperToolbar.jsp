<%--
  Created by IntelliJ IDEA.
  User: Francesco Alongi
  Date: 03/06/2018
  Time: 18:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/Stylesheet/stylesheet.css" type="text/css">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<nav class="navbar navbar-default" style="background-color: #e3f2fd;">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="${pageContext.request.contextPath}">CrowdsourcingPeak</a>
        </div>

            <ul class = "nav navbar-nav navbar-right" id="ulNavBar">
                <li><a href="${pageContext.request.contextPath}/Logout"><span class="glyphicon glyphicon-log-out"></span>Logout</a></li>
            </ul>
    </div>
</nav>
