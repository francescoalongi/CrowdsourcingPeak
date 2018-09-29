<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Campaign" %>
<%@ page import="Model.Manager" %>
<%--
  Created by IntelliJ IDEA.
  Manager: Francesco Alongi
  Date: 27/05/2018
  Time: 15:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manager Home Page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Stylesheet/stylesheet.css" type="text/css">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body onload="addNavButton()">

<jsp:include page="/WEB-INF/View/UpperToolbar.jsp"/>

<div class="container">
    <div class="row">
        <h2>Welcome, <%=((Manager)(session.getAttribute("user"))).getUsername()%>.</h2>
        <%
            ArrayList<Campaign> campaigns = (ArrayList<Campaign>) request.getAttribute("createdCampaigns");
            if(campaigns ==  null || campaigns.isEmpty()) {
        %>
        <h4>You have not created campaign yet.</h4>
        <%
        } else {
        %>
        <h4>Created campaigns</h4>
        <div class="list-group">

            <% for(Campaign campaign : campaigns){ %>

                <div class="col-md-4">
                    <a href="${pageContext.request.contextPath}/LoadCampaignData?idCampaign=<%= campaign.getIdCampaign()%>" class="list-group-item list-group-item-action flex-column align-items-start">
                        <div class="d-flex w-100 justify-content-between">
                            <h5 class="mb-1"><%= campaign.getName() %></h5>
                        </div>
                        <p class="mb-1"> <%if (campaign.getStartDate() != null) {%> Start date: <%= campaign.getStartDate() %> <br/> <%} if (campaign.getEndDate() != null) {%> End date:   <%=campaign.getEndDate() %> <%}%></p>
                        <small class="text-muted"><%= campaign.getState() %></small>
                    </a>
                </div>

            <%
                }
            %>
        <%
            }
        %>
        </div>
    </div>
</div>
</body>
<script>
    function addNavButton() {
        var ul = document.getElementById("ulNavBar");
        var createCampaignLi = document.createElement("li");
        var editProfileDataLi = document.createElement("li");
        createCampaignLi.setAttribute("class","liMargin");
        createCampaignLi.innerHTML = "<li><a class=\"btn btn-info\" href=\"${pageContext.request.contextPath}/Manager/CreateCampaign\">Create new campaign</a></li>";
        editProfileDataLi.innerHTML = "<a href=\"${pageContext.request.contextPath}/User/EditPersonalData\"><span class=\"glyphicon glyphicon-user\"></span>Edit profile data</a>";
        ul.insertBefore(editProfileDataLi, ul.firstElementChild);
        ul.insertBefore(createCampaignLi,ul.firstElementChild);
    }
</script>
</html>
