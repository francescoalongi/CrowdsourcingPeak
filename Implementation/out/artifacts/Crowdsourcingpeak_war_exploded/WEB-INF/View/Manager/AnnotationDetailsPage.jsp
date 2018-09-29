<%@ page import="java.util.Map" %>
<%@ page import="Model.Annotation" %>
<%@ page import="Model.Worker" %>
<%@ page import="Model.LocalizedName" %><%--
  Created by IntelliJ IDEA.
  User: Francesco Alongi
  Date: 31/08/2018
  Time: 11:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Annotations Details</title>
</head>
<body>
<jsp:include page="/WEB-INF/View/UpperToolbar.jsp"/>
<div class="container">
    <div class="row">
        <div class="col">
            <h2>Annotations Details:</h2>
        </div>
    </div>

            <%
                Map<Worker,Annotation> workerAnnotationMap = (Map<Worker,Annotation>)request.getAttribute("workerAnnotationMap");
                if (workerAnnotationMap != null) {
                    for (Map.Entry<Worker, Annotation> entry : workerAnnotationMap.entrySet()) {
            %>
    <div class="row">
        <div class="col">
                        <dl>
                            <dt class="col-md-3">Annotation id:</dt>
                            <dd class="col-md-9"><%=entry.getValue().getIdAnnotation()%></dd>
                            <dt class="col-md-3">Id creator:</dt>
                            <dd class="col-md-9"><%=entry.getKey().getIdUser()%></dd>
                            <dt class="col-md-3">Username creator:</dt>
                            <dd class="col-md-9"><%=entry.getKey().getUsername()%></dd>
                            <dt class="col-md-3">E-mail creator:</dt>
                            <dd class="col-md-9"><%=entry.getKey().getEmail()%></dd>
                            <dt class="col-md-3">Date of creation:</dt>
                            <dd class="col-md-9"><%=entry.getValue().getCreationDate()%></dd>
                            <%if (entry.getValue().getName() != null && !entry.getValue().getName().isEmpty()) {%>
                                <dt class="col-md-3">Name:</dt>
                                <dd class="col-md-9"><%=entry.getValue().getName()%></dd>
                            <%}%>
                            <%if (entry.getValue().getElevation() != null){%>
                                <dt class="col-md-3">Altitude:</dt>
                                <dd class="col-md-9"><%=entry.getValue().getElevation()%></dd>
                            <%}%>
                            <%if (!entry.getValue().getLocalizedNames().isEmpty()) {%>
                            <dt class="col-md-12">Localized Names:</dt>
                            <dd class="col-md-12">
                                <dl>
                                    <%for (LocalizedName localizedName : entry.getValue().getLocalizedNames()) {%>
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
                                <%if (entry.getValue().getValidity()) {%>
                                Valid
                                <%} else {%>
                                Not valid
                                <%}%>
                            </dd>
                            <dt class="col-md-3">State:</dt>
                            <dd class="col-md-9">
                                <%if (entry.getValue().getState()) {%>
                                Not refused
                                <%} else {%>
                                Refused
                                <%}%>
                            </dd>
                        </dl>
        </div>
    </div>
            <%
                    }
                }
            %>

</div>
</body>
</html>
