<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Campaign" %>
<%@ page import="Model.Worker" %>
<%--
  Created by IntelliJ IDEA.
  User: FedRhRh
  Date: 06/06/2018
  Time: 13:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Worker Home Page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Stylesheet/stylesheet.css" type="text/css">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body onload="addNavButton()">

<jsp:include page="/WEB-INF/View/UpperToolbar.jsp"/>

<div class="container">
    <div class="row">
        <h2>Welcome, <%=((Worker)session.getAttribute("user")).getUsername()%>.</h2>
        <%
            ArrayList<Campaign> regCampaigns = (ArrayList<Campaign>) request.getAttribute("registeredCampaigns");
            if(regCampaigns ==  null || regCampaigns.isEmpty()) {
        %>
        <h4>You are not registered to any campaign yet.</h4>
        <%
        } else {
        %>
        <h4>Registered campaigns</h4>
        <div class="list-group">

            <% for(Campaign campaign : regCampaigns){ %>

            <div class="col-md-4">
                <%-- da linkare quando viene premuto --%>
                <a href="${pageContext.request.contextPath}/LoadCampaignPeaksData?idCampaign=<%= campaign.getIdCampaign()%>&forwardTo=2D" class="list-group-item list-group-item-action flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1"><%= campaign.getName() %></h5>
                    </div>
                    <p class="mb-1">  <%if (campaign.getStartDate() != null) {%> Start date: <%= campaign.getStartDate() %> <br/> <%} if (campaign.getEndDate() != null) {%> End date:   <%=campaign.getEndDate() %> <%}%></p>
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
<br/>
<br/>
<div class="container">
    <div class="row">
        <%
            ArrayList<Campaign> avCampaigns = (ArrayList<Campaign>) request.getAttribute("availableCampaigns");
            if(avCampaigns ==  null || avCampaigns.isEmpty()) {
        %>
        <h4>No campaigns available.</h4>
        <%
        } else {
        %>
        <h4>Available campaigns</h4>
        <div class="list-group">

            <% for(Campaign campaign : avCampaigns){ %>

            <div class="col-md-4">
                <%-- da linkare quando viene premuto --%>
                <a href="${pageContext.request.contextPath}/RegisterToCampaign?idCampaign=<%= campaign.getIdCampaign()%>&forwardTo=2D" class="list-group-item list-group-item-action flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1"><%= campaign.getName() %></h5>
                    </div>
                    <p class="mb-1">  <%if (campaign.getStartDate() != null) {%> Start date: <%= campaign.getStartDate() %> <br/> <%} if (campaign.getEndDate() != null) {%> End date:   <%=campaign.getEndDate() %> <%}%></p>
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
<script>
    function addNavButton() {
        var ul = document.getElementById("ulNavBar");
        var editProfileDataLi = document.createElement("li");
        editProfileDataLi.innerHTML = "<a href=\"${pageContext.request.contextPath}/User/EditPersonalData\"><span class=\"glyphicon glyphicon-user\"></span>Edit profile data</a>";
        ul.insertBefore(editProfileDataLi, ul.firstElementChild);
    }
</script>
</body>
</html>
