<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Consume Progress</title>
  <%@include file="../include/base-path.jsp"%>
  <base href="<%=basePath%>%">
  <link rel="shortcut icon" href="favicon.ico" />
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <script src="http://libs.baidu.com/jquery/1.7.0/jquery.js"></script>
  <script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
  <link href="http://libs.baidu.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet">
  <script type="text/javascript" src="http://cdn.hcharts.cn/jquery/jquery-1.8.3.min.js"></script>
  <script type="text/javascript" src="js/highcharts/highcharts.js"></script>
  <script type="text/javascript" src="js/highcharts/modules/exporting.js"></script>
  <script src="js/consume-progress.js" type="application/javascript"></script>
</head>
<body>

<div class="container-fluid">
  <div class="row">
    <div class="col-md-8 col-md-offset-2 text-center">
      <h1>Consume Progress Catalog</h1>
    </div>
  </div>
  <table class="table table-bordered">
<tr><td>
<div id="selConsumerGroup" style="display: inline"></div><div id="selTopic" style="display:none"></div><div id="selBroker" style="display:none"></div><div id="selQueue" style="display:none"></div>
</td></tr>
<tr><td>
<div class="col-xs-2">
      <button type="submit" class="btn btn-primary findConsumeProgress">find</button>
    </div>
</td></tr>
<tr><td>
<div id="container" style="min-width:700px;height:400px"></div>
</td></tr>
  </table>
</div>
</body>
</html>
