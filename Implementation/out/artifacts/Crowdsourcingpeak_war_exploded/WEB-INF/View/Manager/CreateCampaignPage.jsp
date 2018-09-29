<%--
  Created by IntelliJ IDEA.
  User: nicologhielmetti
  Date: 04/06/2018
  Time: 17:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Campaign</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <!--
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/css/bootstrap-datetimepicker.min.css" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.6/moment.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/js/bootstrap-datetimepicker.min.js"></script>
    -->
</head>
<body>
<jsp:include page="/WEB-INF/View/UpperToolbar.jsp"/>
<div class="container">
    <h1>In this page, you can create a new campaign!</h1>
    <h3>Type here the name of the campaign</h3>
    <br>
    <% if (request.getAttribute("errorInformation") != null) { %>

    <div class="alert alert-warning">
        <%=request.getAttribute("errorInformation")%>
    </div>

    <%}%>

    <form class="form-horizontal" method="POST" action="${pageContext.request.contextPath}/CreateCampaign" id="createCampaignForm">

        <div class="form-group">
            <div class="col-md-4">
                <label for="name">Name:</label>
                <input type="text" name="name" id="name" placeholder="Enter name" class="form-control">
            </div>
        </div>
<!--
        <div class="form-group">
            <div class="col-md-4">
                <label for="datetimepicker1">Start date:</label>
                <div class='input-group date' id='datetimepicker1'>
                    <input type='text' class="form-control" name="startDate" id="startDate" placeholder="Start date"/>
                    <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="col-md-4">
                <label for="datetimepicker2">End date:</label>
                <div class='input-group date' id='datetimepicker2'>
                    <input type='text' class="form-control" name="endDate" id="endDate" placeholder="End date"/>
                    <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                </div>
            </div>
        </div>
-->
        <div class="form-group">
            <div class="col-xs-5">
                <button type="submit" class="btn btn-default">Validate</button>
            </div>
        </div>

    </form>
</div>
</body>
<!--<script type="text/javascript">
    $(function () {
        $('#datetimepicker1').datetimepicker({
            format:'DD/MM/YYYY HH:mm:ss'
        });
        $('#datetimepicker2').datetimepicker({
            useCurrent: false, //Important! See issue #1075
            format:'DD/MM/YYYY HH:mm:ss'
        });
        $("#datetimepicker1").on("dp.change", function (e) {
            $('#datetimepicker2').data("DateTimePicker").minDate(e.date);
        });
        $("#datetimepicker2").on("dp.change", function (e) {
            $('#datetimepicker1').data("DateTimePicker").maxDate(e.date);
        });
    });
</script>-->
</html>
