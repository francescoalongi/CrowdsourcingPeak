<%@ page import="Model.Peak" %>
<%@ page import="Model.LocalizedName" %><%--
  Created by IntelliJ IDEA.
  User: Francesco Alongi
  Date: 31/08/2018
  Time: 15:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Peak details and annotation</title>
</head>
<body onload="disableAnnotationForm()">
<jsp:include page="/WEB-INF/View/UpperToolbar.jsp"/>
<%
    Peak peak = (Peak)request.getAttribute("peak");
%>

<div class="container">
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
    <div class="row">
        <div class="col-md-6">

<%if (!(Boolean)request.getAttribute("isPeakAlreadyAnnotated")) {%>

            <h3>Create here your annotation for this peak:</h3>
            <% if (request.getAttribute("errorInformation") != null) { %>

            <div class="alert alert-warning">
                <%=request.getAttribute("errorInformation")%>
            </div>

            <%}%>
            <p>Is this a valid peak?</p>

            <form method ="POST" action="${pageContext.request.contextPath}/CreateAnnotation" id ="annotationForm">
                <input type="hidden" name="idCampaign" value="<%=request.getAttribute("idCampaign")%>">
                <input type="hidden" name="idPeak" value="<%=((Peak) request.getAttribute("peak")).getIdPeak()%>">
                <input type="hidden" name="forwardTo" value="2D">
                <label class="radio-inline">
                    <input type="radio" name="optradio" id="validRadio" onchange="enableDisableForm(this)" value="valid">Valid
                </label>
                <label class="radio-inline">
                    <input type="radio" name="optradio" id="invalidRadio" onchange="enableDisableForm(this)" value="invalid">Not valid
                </label>
                <fieldset id="inputTextFieldset">
                    <div class="form-group">
                        <label for="name">Name:</label>
                        <input class="form-control" type="text" name="name" id="name" placeholder="Enter the name of the peak">
                    </div>
                    <div class="form-group">
                        <label for="altitude">Altitude:</label>
                        <input type="text" class="form-control" name="altitude" id="altitude" placeholder="Enter the altitude of the peak (m)">
                    </div>
                    <button type="button" class="btn" onclick="addLocalizedNameForm()">Add localized name</button>
                    <button type="button" class="btn" onclick="deleteLocalizedNameForm()">Delete localized name</button>
                </fieldset>
                <fieldset id="submitFieldset">
                    <input type="submit" value="Submit" class="btn btn-primary">
                </fieldset>
            </form>

<%} else {%>
    <h3>You have already annotated this peak!</h3>
<%}%>
        </div>
    </div>
</div>
<script>
    function disableAnnotationForm() {
        var inputTextFieldset = document.getElementById("inputTextFieldset");
        var submitFieldset = document.getElementById("submitFieldset");
        inputTextFieldset.setAttribute("disabled","disabled");
        submitFieldset.setAttribute("disabled","disabled");
        inputTextFieldset.lastElementChild.disabled = true;
    }

    function enableDisableForm(radioButton) {
        var inputTextFieldset = document.getElementById("inputTextFieldset");
        var submitFieldset = document.getElementById("submitFieldset");
        submitFieldset.removeAttribute("disabled");

        if (radioButton.id === "validRadio") {
            inputTextFieldset.removeAttribute("disabled");
        } else if (radioButton.id === "invalidRadio") {
            inputTextFieldset.setAttribute("disabled","disabled");
        }
    }

    var i = 0;
    function addLocalizedNameForm() {
        var inputTextFieldset = document.getElementById("inputTextFieldset");
        var divCountry = document.createElement("div");
        divCountry.setAttribute("class","form-group");
        divCountry.setAttribute("id", "divCountry" + i);
        var divLocalizedName = document.createElement("div");
        divLocalizedName.setAttribute("class","form-group");
        divLocalizedName.setAttribute("id", "divLocalizedName" + i);
        var inputTextCountry = document.createElement("input");
        inputTextCountry.setAttribute("type","text");
        inputTextCountry.setAttribute("class","form-control");
        inputTextCountry.setAttribute("name", "country" + i);
        inputTextCountry.setAttribute("id","country" + i);
        inputTextCountry.setAttribute("placeholder", "Enter the country of the localized name");
        var labelCountry = document.createElement("label");
        labelCountry.setAttribute("for","country" + i);
        labelCountry.innerText = "Country:";
        var inputTextLocalizedName = document.createElement("input");
        inputTextLocalizedName.setAttribute("type","text");
        inputTextLocalizedName.setAttribute("class","form-control");
        inputTextLocalizedName.setAttribute("name", "localizedName" + i);
        inputTextLocalizedName.setAttribute("id","localizedName" + i);
        inputTextLocalizedName.setAttribute("placeholder", "Enter the localized name");
        var labelLocalizedName = document.createElement("label");
        labelLocalizedName.setAttribute("for", "localizedName" + i)
        labelLocalizedName.innerText = "Localized name:";
        divCountry.appendChild(labelCountry)
        divCountry.appendChild(inputTextCountry);
        divLocalizedName.appendChild(labelLocalizedName);
        divLocalizedName.appendChild(inputTextLocalizedName);

        inputTextFieldset.insertBefore(divCountry, inputTextFieldset.children[inputTextFieldset.children.length - 2]);
        inputTextFieldset.insertBefore(divLocalizedName, inputTextFieldset.children[inputTextFieldset.children.length - 2]);
        inputTextFieldset.lastElementChild.disabled = false;
        i++;
    }

    function deleteLocalizedNameForm() {
        i--;
        var lastDivCountry = document.getElementById("divCountry" + i);
        var lastDivLocalizedName = document.getElementById("divLocalizedName" + i);
        if (i === 0) lastDivCountry.parentElement.lastElementChild.disabled = true;
        lastDivCountry.parentNode.removeChild(lastDivCountry);
        lastDivLocalizedName.parentNode.removeChild(lastDivLocalizedName);
    }

</script>
</body>
</html>
