<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Peak" %>
<%--
  Created by IntelliJ IDEA.
  User: FedRhRh
  Date: 07/06/2018
  Time: 17:15
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Worker 2D Map</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Stylesheet/MapStylesheet.css"/>
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.4/dist/leaflet.css"
          integrity="sha512-puBpdR0798OZvTTbP4A8Ix/l+A4dHDD0DGqYW6RQ+9jxkRFclaxxQb/SJAWZfWAkuyeQUytO7+7N4QKrDh+drA=="
          crossorigin=""/>
    <link rel="stylesheet" href="https://unpkg.com/leaflet.markercluster@1.4.0/dist/MarkerCluster.css"/>
    <link rel="stylesheet" href="https://unpkg.com/leaflet.markercluster@1.4.0/dist/MarkerCluster.Default.css"/>

</head>
<body onload="addNavButton()">
<div class="outer">
<jsp:include page="/WEB-INF/View/UpperToolbar.jsp"/>

<div id="mapid"></div>
<script src="https://unpkg.com/leaflet@1.3.4/dist/leaflet.js"
        integrity="sha512-nMMmRyTVoLYqjP9hrbed9S+FzjZHW5gY1TWCHA5ckwXZBadntCNs8kEqAWdrb9O7rxbCaA4lKTIWjDXZxflOcA=="
        crossorigin=""></script>

<script src="https://unpkg.com/leaflet.markercluster@1.4.0/dist/leaflet.markercluster.js"></script>
<script>

    // by default the zoom is set over Europe
    var mymap = L.map('mapid').setView([41.9109, 12.4818], 5);

    L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
        attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
        maxZoom: 18,
        id: 'mapbox.streets',
        accessToken: 'pk.eyJ1IjoiZnJhbmNlc2NvYWwiLCJhIjoiY2psZHQwMjI0MGRrODN3cDcyYWFxZTM2eSJ9.LSFI4klzw9jmV8Mwu7I_wg'
    }).addTo(mymap);

    <%
        // find the right-top corner and bottom-left corner in order to zoom correctly the map
        if (!((ArrayList<Peak>)request.getAttribute("campaignPeaks")).isEmpty()) {
        ArrayList<Peak> campaignPeaks =(ArrayList<Peak>) request.getAttribute("campaignPeaks");

        Peak rightMostPeak = campaignPeaks.get(0);
        Peak topMostPeak = campaignPeaks.get(0);
        Peak leftMostPeak = campaignPeaks.get(0);
        Peak bottomMostPeak = campaignPeaks.get(0);

        for (Peak peak : campaignPeaks)
        {
            if (peak.getLongitude() < rightMostPeak.getLongitude())
                rightMostPeak = peak;
            if (peak.getLongitude() > leftMostPeak.getLongitude())
                leftMostPeak = peak;
            if (peak.getLatitude() > topMostPeak.getLatitude())
                topMostPeak = peak;
            if (peak.getLatitude() < bottomMostPeak.getLatitude())
                bottomMostPeak = peak;

        }

    %>

    mymap.fitBounds([
        [<%=topMostPeak.getLatitude()%>, <%=rightMostPeak.getLongitude()%>],
        [<%=bottomMostPeak.getLatitude()%>, <%=leftMostPeak.getLongitude()%>]
    ]);

    var greenIcon = new L.Icon({
        iconUrl: 'https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',
        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41]
    });

    var yellowIcon = new L.Icon({
        iconUrl: 'https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-yellow.png',
        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41]
    });

    var markers = L.markerClusterGroup();
    <%
        for (Peak peak : campaignPeaks) {
    %>
    <%
    if (peak.getToBeAnnotated()) {%>
    var yellowMarker = L.marker([<%=peak.getLatitude()%>, <%=peak.getLongitude()%>], {icon: yellowIcon});
    yellowMarker.properties = {};
    yellowMarker.properties.idPeak = <%=peak.getIdPeak()%>;
    yellowMarker.properties.lat = <%=peak.getLatitude()%>;
    yellowMarker.properties.lng = <%=peak.getLongitude()%>;
    yellowMarker.properties.annotable = <%=peak.getToBeAnnotated()%>
    markers.addLayer(yellowMarker);
   <% } else {%>
    var greenMarker = L.marker([<%=peak.getLatitude()%>, <%=peak.getLongitude()%>], {icon: greenIcon});
    greenMarker.properties = {};
    greenMarker.properties.idPeak = <%=peak.getIdPeak()%>;
    greenMarker.properties.lat = <%=peak.getLatitude()%>;
    greenMarker.properties.lng = <%=peak.getLongitude()%>;
    greenMarker.properties.annotable = <%=peak.getToBeAnnotated()%>
    markers.addLayer(greenMarker);
    <%
    }
    %>
    <%
    }
    %>
    mymap.addLayer(markers);

    markers.on("click", function (event) {
        var clickedMarker = event.layer;
        if (clickedMarker.properties.annotable) {
            clickedMarker.bindPopup("<dl><dt>Peak id: </dt><dd>" + clickedMarker.properties.idPeak + "</dd><dt>Latitude: </dt><dd>" + clickedMarker.properties.lat +
                "</dd><dt>Longitude:</dt><dd>" + clickedMarker.properties.lng + "</dd></dl><form method=\"GET\" action=\"${pageContext.request.contextPath}/LoadPeakWorkerData\"><div class=\"form-group\"><input type=\"hidden\" name=\"idPeak\" value=\"" + clickedMarker.properties.idPeak + "\"><input type=\"hidden\" name=\"idCampaign\" value=\"" + <%=request.getParameter("idCampaign")%> + "\"><input type=\"submit\" class=\"btn btn-primary\" value=\"Create annotation\"></div></form>").openPopup()
        } else {
            clickedMarker.bindPopup("<dl><dt>Peak id: </dt><dd>" + clickedMarker.properties.idPeak + "</dd><dt>Latitude: </dt><dd>" + clickedMarker.properties.lat +
                "</dd><dt>Longitude:</dt><dd>" + clickedMarker.properties.lng + "</dd></dl>").openPopup()

        }
    });

    <%}%>
</script>
<script>
    function addNavButton() {
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
        hiddenInputMapTypeForm.setAttribute("value", "3D");

        var submitInputMapForm = document.createElement("input");
        submitInputMapForm.setAttribute("type","submit");
        submitInputMapForm.setAttribute("class","btn btn-light");
        submitInputMapForm.setAttribute("value","View 3D map");

        viewMapForm.appendChild(hiddenInputMapForm);
        viewMapForm.appendChild(hiddenInputMapTypeForm);
        viewMapForm.appendChild(submitInputMapForm);

        var li = document.createElement("li");
        li.setAttribute("class","liMargin");
        li.appendChild(viewMapForm);
        ul.insertBefore(li, ul.firstElementChild);
    }
</script>
</div>
</body>
</html>

