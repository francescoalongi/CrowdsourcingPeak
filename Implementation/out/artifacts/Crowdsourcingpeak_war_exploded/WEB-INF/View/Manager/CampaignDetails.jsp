<%--
  Created by IntelliJ IDEA.
  User: nicologhielmetti
  Date: 04/06/2018
  Time: 17:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Campaign Details</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Stylesheet/stylesheet.css">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body onload="addNavButton()" >
<jsp:include page="/WEB-INF/View/UpperToolbar.jsp"/>
<div class="container">
    <div class="row">
        <div class="col">
            <h1>Details of the campaign <%=request.getAttribute("nameCampaign")%></h1>
            <%
                String state = (String) request.getAttribute("stateCampaign");
                String bsClass = null;
                if (state.equals("CREATED")) bsClass = "text-muted";
                if (state.equals("STARTED")) bsClass = "text-success";
                if (state.equals("CLOSED")) bsClass = "text-danger";
            %>
            <div class="row">
                <h4>
                    <dl>
                        <div class="col-md-6">
                            <dt class="col-md-3">State:</dt>
                            <dd class="<%=bsClass%> col-md-9"><%=state%></dd>
                            <%if (request.getAttribute("startDateCampaign") != null) {%>
                            <dt class="col-md-3">Start date:</dt>
                            <dd class="col-md-9"><%=request.getAttribute("startDateCampaign")%></dd>
                            <%}%>
                            <%if (request.getAttribute("endDateCampaign") != null){%>
                            <dt class="col-md-3">End date:</dt>
                            <dd class="col-md-9"><%=request.getAttribute("endDateCampaign")%></dd>
                            <%}%>
                        </div>
                    </dl>
                </h4>
            </div>
            <br>
        </div>
    </div>
    <%if (state.equals("CREATED")) {%>

    <% if (request.getAttribute("errorInformation") != null) { %>
    <div class="alert alert-warning">
        <%=request.getAttribute("errorInformation")%>
    </div>
    <% } %>

    <div class="row">
        <div class="col-md-6">

            <form method="POST" action="${pageContext.request.contextPath}/InsertPeaks" enctype="multipart/form-data">
                <input type="hidden" name="idCampaign" value="<%=request.getParameter("idCampaign")%>">
                <input type="hidden" name="stateCampaign" value="<%=request.getAttribute("stateCampaign")%>">
                <input type="hidden" name="startDateCampaign" value="<%=request.getAttribute("startDateCampaign")%>">
                <input type="hidden" name="endDateCampaign" value="<%=request.getAttribute("endDateCampaign")%>">
                <input type="hidden" name="nameCampaign" value="<%=request.getAttribute("nameCampaign")%>">
                <input type="hidden" name="forwardTo" value="2D">

                <div class="form-group">
                    <label for="inputGroupFile">Choose the file you want to load</label>
                    <input type="file" class="form-control-file" id="inputGroupFile" name="peaksFile">
                </div>
                <div class="form-group form-check">
                    <label class="form-check-label">
                        <input type="checkbox" class="form-check-input" name="toBeAnnotated" value="toBeAnnotated"> To be annotated
                    </label>
                </div>
                <div class="form-group">
                    <input type="submit" class="btn btn-primary" value="Submit">
                </div>
            </form>
        </div>
    </div>
    <%}%>

</div>

<script>

    function addNavButton(){
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
        hiddenInputMapTypeForm.setAttribute("value", "2D");

        var submitInputMapForm = document.createElement("input");
        submitInputMapForm.setAttribute("type","submit");
        submitInputMapForm.setAttribute("class","btn btn-light");
        submitInputMapForm.setAttribute("value","View map");

        viewMapForm.appendChild(hiddenInputMapForm);
        viewMapForm.appendChild(hiddenInputMapTypeForm);
        viewMapForm.appendChild(submitInputMapForm);

        var li = document.createElement("li");
        li.setAttribute("class","liMargin");
        li.appendChild(viewMapForm);
        ul.insertBefore(li, ul.firstElementChild);

        if (<%=request.getAttribute("stateCampaign").equals("STARTED")%> || <%=request.getAttribute("stateCampaign").equals("CLOSED")%>) {
            var viewStatsForm = document.createElement("form");
            viewStatsForm.setAttribute("method","GET");
            viewStatsForm.setAttribute("action","${pageContext.request.contextPath}/LoadCampaignStats");

            var hiddenInputStatsForm = document.createElement("input");
            hiddenInputStatsForm.setAttribute("type","hidden");
            hiddenInputStatsForm.setAttribute("name","idCampaign");
            hiddenInputStatsForm.setAttribute("value", <%=request.getParameter("idCampaign")%>);

            var submitInputStatsForm = document.createElement("input");
            submitInputStatsForm.setAttribute("type","submit");
            submitInputStatsForm.setAttribute("class","btn btn-light");
            submitInputStatsForm.setAttribute("value","View campaign stats");

            viewStatsForm.appendChild(hiddenInputStatsForm);
            viewStatsForm.appendChild(submitInputStatsForm);

            var li = document.createElement("li");
            li.setAttribute("class","liMargin");
            li.appendChild(viewStatsForm);
            ul.insertBefore(li, ul.firstElementChild);
        }

        var campaignLifecycleForm = document.createElement("form");
        campaignLifecycleForm.setAttribute("method", "POST");

        var hiddenInputCampaignForm = document.createElement("input");
        hiddenInputCampaignForm.setAttribute("type","hidden");
        hiddenInputCampaignForm.setAttribute("name", "idCampaign");
        hiddenInputCampaignForm.setAttribute("value", <%=request.getParameter("idCampaign")%>);

        var submitInputCampaignForm = document.createElement("input");
        submitInputCampaignForm.setAttribute("type","submit");

        if (<%=request.getAttribute("stateCampaign").equals("CREATED")%>) {
            campaignLifecycleForm.setAttribute("action", "${pageContext.request.contextPath}/StartCampaign");

            submitInputCampaignForm.setAttribute("class","btn btn-success");
            submitInputCampaignForm.setAttribute("name","start");
            submitInputCampaignForm.setAttribute("value","Start campaign");

            campaignLifecycleForm.appendChild(hiddenInputCampaignForm);
            campaignLifecycleForm.appendChild(submitInputCampaignForm);

            var li = document.createElement("li");
            li.setAttribute("class","liMargin");
            li.appendChild(campaignLifecycleForm);
            ul.insertBefore(li, ul.firstElementChild);

        } else if (<%=request.getAttribute("stateCampaign").equals("STARTED")%>) {
            campaignLifecycleForm.setAttribute("action", "${pageContext.request.contextPath}/CloseCampaign");

            submitInputCampaignForm.setAttribute("class","btn btn-danger");
            submitInputCampaignForm.setAttribute("name","close");
            submitInputCampaignForm.setAttribute("value","Close campaign");

            campaignLifecycleForm.appendChild(hiddenInputCampaignForm);
            campaignLifecycleForm.appendChild(submitInputCampaignForm);

            var li = document.createElement("li");
            li.setAttribute("class","liMargin");
            li.appendChild(campaignLifecycleForm);
            ul.insertBefore(li, ul.firstElementChild);
        }
    }
</script>
</body>
</html>
