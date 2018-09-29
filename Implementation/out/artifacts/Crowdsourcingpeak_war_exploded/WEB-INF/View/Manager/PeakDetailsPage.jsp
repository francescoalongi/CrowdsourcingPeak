<%@ page import="Model.Peak" %>
<%@ page import="Model.LocalizedName" %>
<%@ page import="Model.Annotation" %>
<%@ page import="java.util.ArrayList" %>
<%--
  Created by IntelliJ IDEA.
  User: Francesco Alongi
  Date: 29/08/2018
  Time: 15:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Peak Details</title>
</head>
<body onload="hideSubmit()">
<jsp:include page="/WEB-INF/View/UpperToolbar.jsp"/>

<%
    Peak peak = (Peak)request.getAttribute("peak");
    ArrayList<Annotation> annotations = (ArrayList<Annotation>) request.getAttribute("annotations");
%>

<div class="container">

    <!-- when is impossible to update the annotations the ChangeAnnotationStateServlet forward the request to LoadPeakManagerDataServlet which
         forward to this page and show the error-->
    <% if (request.getAttribute("errorInformation") != null) { %>
    <div class="alert alert-warning">
        <%=request.getAttribute("errorInformation")%>
    </div>
    <% } %>

    <div class = "row">
        <div class="col">
            <h1>Details of the selected peak:</h1>
        </div>
    </div>

    <div class="row">
        <div class="col">
            <dl>
                <dt class="col-md-3">Peak Id:</dt>
                <dd class="col-md-9"><%=peak.getIdPeak()%></dd>
                <dt class="col-md-3">Datasource:</dt>
                <dd class="col-md-9"><%=peak.getProvenance()%></dd>
                <dt class="col-md-3">Datasource peak Id:</dt>
                <dd class="col-md-9"><%=peak.getGivenIdPeak()%></dd>
                <%if (peak.getName() != null) {%>
                <dt class="col-md-3">Name:</dt>
                <dd class="col-md-9"><%=peak.getName()%></dd>
                <%}%>
                <%if (peak.getAltitude() != null) {%>
                <dt class="col-md-3">Altitude:</dt>
                <dd class="col-md-9"><%=peak.getAltitude()%> m</dd>
                <%}%>
                <dt class="col-md-3">Latitude:</dt>
                <dd class="col-md-9"><%=peak.getLatitude()%>°</dd>
                <dt class="col-md-3">Longitude:</dt>
                <dd class="col-md-9"><%=peak.getLongitude()%>°</dd>
                <%if (!peak.getLocalizedNames().isEmpty()) {%>
                <dt class="col-md-12">Localized Names:</dt>
                <dd class="col-md-12">
                    <dl>
                        <%for (LocalizedName localizedName : peak.getLocalizedNames()) {%>
                        <dt class="col-md-3">Country: </dt>
                        <dd class="col-md-9"><%=localizedName.getCountry()%></dd>
                        <dt class="col-md-3">Localized name: </dt>
                        <dd class="col-md-9"><%=localizedName.getLocalizedName()%></dd>
                        <%}%>
                    </dl>
                </dd>
                <%}%>
            </dl>
        </div>
    </div>

    <%if (!annotations.isEmpty()) {%>
    <div class = "row">
        <div class = "col">
            <h3>Annotations of the selected peak:</h3>
        </div>
    </div>

    <form method="post" action="${pageContext.request.contextPath}/ChangeAnnotationState">
        <input type="hidden" name="idCampaign" value="<%=request.getParameter("idCampaign")%>">
        <input type="hidden" name="idPeak" value="<%=request.getParameter("idPeak")%>">
        <input type="hidden" name="forwardTo" value="2D">
        <%for (Annotation annotation : annotations) {%>

    <div class="row marginUnderRow">
        <div class="col">
            <dl>
                <dt class="col-md-3">Annotation id:</dt>
                <dd class="col-md-9"><%=annotation.getIdAnnotation()%></dd>
                <dt class="col-md-3">Made by:</dt>
                <dd class="col-md-9"><%=annotation.getIdWorker()%></dd>
                <dt class="col-md-3">Date of creation:</dt>
                <dd class="col-md-9"><%=annotation.getCreationDate()%></dd>
                <%if (annotation.getName() != null && !annotation.getName().isEmpty()) {%>
                    <dt class="col-md-3">Name:</dt>
                    <dd class="col-md-9"><%=annotation.getName()%></dd>
                <%}%>
                <%if (annotation.getElevation() != null) {%>
                    <dt class="col-md-3">Altitude:</dt>
                    <dd class="col-md-9"><%=annotation.getElevation()%></dd>
                <%}%>
                <%if (!annotation.getLocalizedNames().isEmpty()) {%>
                <dt class="col-md-12">Localized Names:</dt>
                <dd class="col-md-12">
                    <dl>
                        <%for (LocalizedName localizedName : annotation.getLocalizedNames()) {%>
                        <dt class="col-md-3">Country: </dt>
                        <dd class="col-md-9"><%=localizedName.getCountry()%></dd>
                        <dt class="col-md-3">Localized name: </dt>
                        <dd class="col-md-9"><%=localizedName.getLocalizedName()%></dd>
                        <%}%>
                    </dl>
                </dd>
                <%}%>
                <dt class="col-md-3">Validity:</dt>
                <dd class="col-md-9">
                    <%if (annotation.getValidity()) {%>
                    Valid
                    <%} else {%>
                    Not valid
                    <%}%>
                </dd>
                <dt class="col-md-3">State:</dt>
                <dd class="col-md-9">
                    <%if (annotation.getState()) {%>
                    Not refused
                    <%} else {%>
                    Refused
                    <%}%>
                </dd>
            </dl>
        </div>
    </div>
        <div class="row marginUnderRow">
            <div class="col-md-6">
                <div class="form-check">
                    <%if (annotation.getState()){%>
                    <div class="col-md-6">
                        <input type="checkbox" class="form-check-input" name="refusedAnnotation" value="<%=annotation.getIdAnnotation()%>" id="<%=annotation.getIdAnnotation()%>refuse" onchange="showHideSubmit(this)">
                        <label class="form-check-label" for="<%=annotation.getIdAnnotation()%>refuse">Refuse annotation</label>
                    </div>
                    <!--<div class="col-md-6">
                        <input type="checkbox" class="form-check-input" name="notRefusedAnnotation" value="<%=annotation.getIdAnnotation()%>" id="<%=annotation.getIdAnnotation()%>notRefuse" disabled="disabled">
                        <label class="form-check-label" for="<%=annotation.getIdAnnotation()%>notRefuse">Don't refuse annotation</label>
                    </div>-->
                    <%} else {%>
                    <!--<div class="col-md-6">
                        <input type="checkbox" class="form-check-input" name="refusedAnnotation" value="<%=annotation.getIdAnnotation()%>" id="<%=annotation.getIdAnnotation()%>" disabled="disabled">
                        <label class="form-check-label" for="<%=annotation.getIdAnnotation()%>refuse">Refuse annotation</label>
                    </div>-->
                    <div class="col-md-6">
                        <input type="checkbox" class="form-check-input" name="notRefusedAnnotation" value="<%=annotation.getIdAnnotation()%>" id="<%=annotation.getIdAnnotation()%>notRefuse" onchange="showHideSubmit(this)">
                        <label class="form-check-label" for="<%=annotation.getIdAnnotation()%>notRefuse">Accept annotation</label>
                    </div>

                    <%}%>
                </div>
            </div>
        </div>
    <%}%>
        <div class="form-group">
            <input type="submit" class="btn btn-primary" value="Submit" id="submitButton">
        </div>
    </form>
    <%} else {%>
    <div class = "row">
        <div class = "col">
            <h3>No annotations yet for the selected peak!</h3>
        </div>
    </div>
    <%}%>
    <br>
</div>
<script>
    function hideSubmit () {
        var submitButton = document.getElementById("submitButton");
        submitButton.style.display = "none";
    }

    var checkboxes = [];
    function showHideSubmit(checkbox) {
        if (checkbox.checked) {
            checkboxes.push("checkbox");
        } else {
            checkboxes.splice(-1,1)
        }

        if (checkboxes.length === 0) document.getElementById("submitButton").style.display = "none";
        else document.getElementById("submitButton").style.display = "block";
    }
</script>
</body>
</html>
