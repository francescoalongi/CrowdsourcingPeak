<%@ page import="Model.Peak" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Annotation" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: Francesco Alongi
  Date: 27/08/2018
  Time: 16:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Manager 2D Map</title>
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
            if (!((Map<Peak,ArrayList<Annotation>>)request.getAttribute("peaksData")).isEmpty()) {
            Map<Peak,ArrayList<Annotation>> peakArrayListMap = (Map<Peak, ArrayList<Annotation>>) request.getAttribute("peaksData");

            Peak rightMostPeak = (Peak) peakArrayListMap.keySet().toArray()[0];
            Peak topMostPeak = (Peak) peakArrayListMap.keySet().toArray()[0];
            Peak leftMostPeak = (Peak) peakArrayListMap.keySet().toArray()[0];
            Peak bottomMostPeak = (Peak) peakArrayListMap.keySet().toArray()[0];

            for (Map.Entry<Peak,ArrayList<Annotation>> entry : peakArrayListMap.entrySet())
            {
                if (entry.getKey().getLongitude() < rightMostPeak.getLongitude())
                    rightMostPeak = entry.getKey();
                if (entry.getKey().getLongitude() > leftMostPeak.getLongitude())
                    leftMostPeak = entry.getKey();
                if (entry.getKey().getLatitude() > topMostPeak.getLatitude())
                    topMostPeak = entry.getKey();
                if (entry.getKey().getLatitude() < bottomMostPeak.getLatitude())
                    bottomMostPeak = entry.getKey();

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

        var redIcon = new L.Icon({
            iconUrl: 'https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
            shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
            iconSize: [25, 41],
            iconAnchor: [12, 41],
            popupAnchor: [1, -34],
            shadowSize: [41, 41]
        });

        var orangeIcon = new L.Icon({
            iconUrl: 'https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-orange.png',
            shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
            iconSize: [25, 41],
            iconAnchor: [12, 41],
            popupAnchor: [1, -34],
            shadowSize: [41, 41]
        });

        var markers = L.markerClusterGroup();
        <%
        for (Map.Entry<Peak,ArrayList<Annotation>> entry : peakArrayListMap.entrySet()) {
        %>
        <%
        // for sure improvable
        boolean red = false;
        for (Annotation annotation : entry.getValue()) {
            if (!annotation.getState()) {
                red = true;
                break;
            }
        }
        if (entry.getKey().getToBeAnnotated() && entry.getValue().isEmpty()) {%>
        var yellowMarker = L.marker([<%=entry.getKey().getLatitude()%>, <%=entry.getKey().getLongitude()%>], {icon: yellowIcon});
        yellowMarker.properties = {};
        yellowMarker.properties.idPeak = <%=entry.getKey().getIdPeak()%>;
        yellowMarker.properties.lat = <%=entry.getKey().getLatitude()%>;
        yellowMarker.properties.lng = <%=entry.getKey().getLongitude()%>;
        markers.addLayer(yellowMarker);
        <% } else if (entry.getKey().getToBeAnnotated() && red) {%>
        var redMarker = L.marker([<%=entry.getKey().getLatitude()%>, <%=entry.getKey().getLongitude()%>], {icon: redIcon});
        redMarker.properties = {};
        redMarker.properties.idPeak = <%=entry.getKey().getIdPeak()%>;
        redMarker.properties.lat = <%=entry.getKey().getLatitude()%>;
        redMarker.properties.lng = <%=entry.getKey().getLongitude()%>;
        markers.addLayer(redMarker);
        <% } else if(!entry.getKey().getToBeAnnotated()){%>
        var greenMarker = L.marker([<%=entry.getKey().getLatitude()%>, <%=entry.getKey().getLongitude()%>], {icon: greenIcon});
        greenMarker.properties = {};
        greenMarker.properties.idPeak = <%=entry.getKey().getIdPeak()%>;
        greenMarker.properties.lat = <%=entry.getKey().getLatitude()%>;
        greenMarker.properties.lng = <%=entry.getKey().getLongitude()%>;
        markers.addLayer(greenMarker);
        <% } else if (entry.getKey().getToBeAnnotated() && !entry.getValue().isEmpty()) {%>
        var orangeMarker = L.marker([<%=entry.getKey().getLatitude()%>, <%=entry.getKey().getLongitude()%>], {icon: orangeIcon});
        orangeMarker.properties = {};
        orangeMarker.properties.idPeak = <%=entry.getKey().getIdPeak()%>;
        orangeMarker.properties.lat = <%=entry.getKey().getLatitude()%>;
        orangeMarker.properties.lng = <%=entry.getKey().getLongitude()%>;
        markers.addLayer(orangeMarker);
        <%
        }
        %>
        <%
        }
        %>
        mymap.addLayer(markers);

        markers.on("click", function (event) {
                var clickedMarker = event.layer;
                clickedMarker.bindPopup("<dl><dt>Peak id: </dt><dd>" + clickedMarker.properties.idPeak + "</dd><dt>Latitude: </dt><dd>" + clickedMarker.properties.lat +
                    "</dd><dt>Longitude:</dt><dd>" + clickedMarker.properties.lng + "</dd></dl><form method=\"GET\" action=\"${pageContext.request.contextPath}/LoadPeakDataManager\"><div class=\"form-group\"><input type=\"hidden\" name=\"idPeak\" value=\"" + clickedMarker.properties.idPeak + "\"><input type=\"hidden\" name=\"idCampaign\" value=\"" + <%=request.getParameter("idCampaign")%> + "\"><input type=\"submit\" class=\"btn btn-primary\" value=\"View more details\"></div></form>").openPopup()
        });

        <%}%>
    </script>
    <script>
        function addNavButton() {
            var ul = document.getElementById("ulNavBar");

            var viewMapForm = document.createElement("form");
            viewMapForm.setAttribute("method", "GET");
            viewMapForm.setAttribute("action", "${pageContext.request.contextPath}/LoadCampaignDataManager");

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
