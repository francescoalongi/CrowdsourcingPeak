<%@ page import="Model.Peak" %>
<%@ page import="java.util.ArrayList" %>
<%--
  Created by IntelliJ IDEA.
  User: Francesco Alongi
  Date: 03/09/2018
  Time: 14:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <!-- Use correct character set. -->
    <meta charset="utf-8">
    <!-- Tell IE to use the latest, best version. -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- Make the application on mobile take up the full browser screen and disable user scaling. -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no">
    <title>Worker 3D Map</title>
    <script src="${pageContext.request.contextPath}/Resources/Cesium3DMap/Build/Cesium/Cesium.js"></script>
    <style>
        @import url(${pageContext.request.contextPath}/Resources/Cesium3DMap/Build/Cesium/Widgets/widgets.css);
        html, body, #cesiumContainer {
            width: 100%; height: 100%; margin: 0; padding: 0; overflow: hidden;
        }
    </style>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Stylesheet/MapStylesheet.css"/>

</head>
<body onload="addNavButton()">
<div class="outer">
<jsp:include page="/WEB-INF/View/UpperToolbar.jsp"/>
<div id="cesiumContainer"></div>
<script>
    function addNavButton(){
        var ul = document.getElementById("ulNavBar");

        var viewMapForm = document.createElement("form");
        viewMapForm.setAttribute("method", "GET");
        viewMapForm.setAttribute("action", "${pageContext.request.contextPath}/LoadCampaignPeaksData");

        var hiddenInputMapForm = document.createElement("input");
        hiddenInputMapForm.setAttribute("type","hidden");
        hiddenInputMapForm.setAttribute("name","idCampaign");
        hiddenInputMapForm.setAttribute("value", <%=request.getParameter("idCampaign")%>);

        var hiddenInputMapTypeForm = document.createElement("input");
        hiddenInputMapTypeForm.setAttribute("type","hidden");
        hiddenInputMapTypeForm.setAttribute("name","forwardTo");
        hiddenInputMapTypeForm.setAttribute("value", "2D");

        var submitInputMapForm = document.createElement("input");
        submitInputMapForm.setAttribute("type","submit");
        submitInputMapForm.setAttribute("class","btn btn-light");
        submitInputMapForm.setAttribute("value","View 2D map");

        viewMapForm.appendChild(hiddenInputMapForm);
        viewMapForm.appendChild(hiddenInputMapTypeForm);
        viewMapForm.appendChild(submitInputMapForm);

        var li = document.createElement("li");
        li.setAttribute("class","liMargin");
        li.appendChild(viewMapForm);
        ul.insertBefore(li, ul.firstElementChild);

    }

    var viewer = new Cesium.Viewer('cesiumContainer');
    viewer.infoBox.frame.sandbox = "allow-same-origin allow-top-navigation allow-pointer-lock allow-popups allow-forms allow-scripts";
    viewer._infoBox.frame.sandbox = "allow-same-origin allow-top-navigation allow-pointer-lock allow-popups allow-forms allow-scripts";
    <%
    if (!((ArrayList<Peak>)request.getAttribute("campaignPeaks")).isEmpty()) {
        ArrayList<Peak> campaignPeaks =(ArrayList<Peak>) request.getAttribute("campaignPeaks");
        for (Peak peak : campaignPeaks) {
            if (peak.getToBeAnnotated()) {%>
    viewer.entities.add({
        name: "Peak id: " + <%=peak.getIdPeak()%>,
        description: "<label>Latitude: <%=peak.getLatitude()%></label><br>" +
        "<label>Longitude: <%=peak.getLongitude()%></label><br><br>" +
        "<button onclick=\"parent.viewDetails(<%=peak.getIdPeak()%>, <%=request.getParameter("idCampaign")%>)\">Create annotation</button>",
        position : Cesium.Cartesian3.fromDegrees(<%=peak.getLongitude()%>, <%=peak.getLatitude()%>),
        point : {
            pixelSize: 5,
            color: Cesium.Color.YELLOW,
            outlineColor: Cesium.Color.WHITE,
            outlineWidth: 0.5
        }
    });
    <% } else {%>
    viewer.entities.add({
        name: "Peak id: " + <%=peak.getIdPeak()%>,
        description: "<label>Latitude: <%=peak.getLatitude()%></label><br>" +
        "<label>Longitude: <%=peak.getLongitude()%></label><br><br>",
        position : Cesium.Cartesian3.fromDegrees(<%=peak.getLongitude()%>, <%=peak.getLatitude()%>),
        point : {
            pixelSize: 5,
            color: Cesium.Color.GREEN,
            outlineColor: Cesium.Color.WHITE,
            outlineWidth: 0.5
        }
    });
    <%
    }
    %>
    <%
    }
    %>

    <%

        }
    %>

    viewer.flyTo(viewer.entities);

    function viewDetails(idPeak, idCampaign) {
        window.location.href= "${pageContext.request.contextPath}/LoadPeakWorkerData?idPeak=" + idPeak +"&idCampaign=" + idCampaign;
    }
</script>
</div>
</body>
</html>
