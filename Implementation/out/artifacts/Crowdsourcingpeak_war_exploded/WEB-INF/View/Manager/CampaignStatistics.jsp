<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Peak" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: nicologhielmetti
  Date: 12/06/2018
  Time: 18:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Campaign Statistics</title>
</head>
<body>
<jsp:include page="/WEB-INF/View/UpperToolbar.jsp"/>
<div class="container">
    <div class="row">
        <div class="col">
            <h1>Statistics of the selected campaign:</h1>
        </div>
    </div>
    <div class="row">
        <div class ="col">
            <dl>
                <dt>Number of not annotable peaks:</dt>
                <dd><%=request.getAttribute("num_peaks_not_annotable")%></dd>
                <dt>Number of annotable peaks without annotation:</dt>
                <dd><%=request.getAttribute("num_peaks_not_annotated_yet")%></dd>
                <dt>Number of peaks with annotation: </dt>
                <dd><%=request.getAttribute("num_peaks_annotated")%></dd>
                <dt>Number of peaks with one or more annotation refused:</dt>
                <dd><%=request.getAttribute("num_peaks_one_or_more_annotation_refused")%></dd>
                <dt>Number of conflicts:</dt>
                <dd><%=request.getAttribute("num_conflicts")%></dd>
            </dl>
        </div>
    </div>

    <%
        ArrayList<Peak> annotatedPeaks = (ArrayList<Peak>)request.getAttribute("annotatedPeaks");
        if (annotatedPeaks != null && !annotatedPeaks.isEmpty()) {
    %>
    <div class="row">
        <div class="col-md-6">
            <h3>List of annotated peaks:</h3>
            <div class="list-group">
            <%for (Peak peak : annotatedPeaks) {%>
                <a href="${pageContext.request.contextPath}/ExtractAnnotationData?idPeak=<%=peak.getIdPeak()%>&idCampaign=<%=request.getParameter("idCampaign")%>" class="list-group-item list-group-item-action">Id peak: <%=peak.getIdPeak()%></a>
            <%}%>
            </div>
        </div>
    </div>
    <%}%>

    <%
        ArrayList<Peak> peaksWithAnnotationRefused = (ArrayList<Peak>)request.getAttribute("peaksWithAnnotationRefused");
        if (peaksWithAnnotationRefused != null && !peaksWithAnnotationRefused.isEmpty()) {
    %>
    <div class="row">
        <div class="col-md-6">
            <h3>List of peaks with annotation refused:</h3>
            <div class="list-group">
                <%for (Peak peak : peaksWithAnnotationRefused) {%>
                <a href="${pageContext.request.contextPath}/ExtractRefusedAnnotationData?idPeak=<%=peak.getIdPeak()%>&idCampaign=<%=request.getParameter("idCampaign")%>" class="list-group-item list-group-item-action">Id peak: <%=peak.getIdPeak()%></a>
                <%}%>
            </div>
        </div>
    </div>
    <%}%>

    <%
        Map<Peak,Integer[]> peakConflictsValidityMap = (Map<Peak,Integer[]> )request.getAttribute("peakConflictsValidityMap");
        if (peakConflictsValidityMap != null && !peakConflictsValidityMap.isEmpty()) {
    %>
    <div class="row">
        <div class="col-md-6">
            <h3>List of peaks with conflicts:</h3>
            <div class="list-group">
                <%for (Map.Entry<Peak, Integer[]> entry : peakConflictsValidityMap.entrySet()) {%>
                <div class="list-group-item">
                    Id peak: <%=entry.getKey().getIdPeak()%>
                    <div class = "row">
                        <div class = "col">
                            <dl>
                                <dt class="col-md-9">Number of annotation with positive validity:</dt>
                                <dd class="col-md-3"><%=entry.getValue()[0]%></dd>
                                <dt class="col-md-9">Number of annotation with negative validity:</dt>
                                <dd class="col-md-3"><%=entry.getValue()[1]%></dd>
                            </dl>
                        </div>
                    </div>
                </div>
                <%}%>
            </div>
        </div>
    </div>
    <%}%>
</div>
</body>
</html>
