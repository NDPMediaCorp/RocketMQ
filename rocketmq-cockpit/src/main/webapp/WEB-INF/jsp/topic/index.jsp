<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Topic Management</title>
  <%@include file="../include/base-path.jsp"%>
  <base href="<%=basePath%>%">
  <link rel="shortcut icon" href="favicon.ico" />
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
  <script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
  <link href="http://libs.baidu.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet">
  <script src="http://cdn.datatables.net/1.10.5/js/jquery.dataTables.min.js"></script>
  <link href="http://cdn.datatables.net/1.10.5/css/jquery.dataTables.min.css" rel="stylesheet">
  <script src="js/topic.js" type="application/javascript"></script>
</head>
<body>
<div class="container-fluid">
  <div class="row">
    <div class="col-md-8 col-md-offset-2 text-center">
      <h1>Topic Catalog</h1>
    </div>
  </div>

      <table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-hover" id="topic" width="90%">
        <thead>
           <tr>
                      <th>topic</th>
                      <th>clusterName</th>
                      <th>brokerAddress</th>
                      <th>writeQueueNum</th>
                      <th>readQueueNum</th>
                      <th>permission</th>
                      <th>unit</th>
                      <th>hasUnitSubscription</th>
                      <th>order</th>
                      <th>status</th>
                      <th>createTime</th>
                      <th>updateTime</th>
                      <th>Operation</th>
            </tr>
        </thead>
        <tbody>
        </tbody>
      </table>


</div>

  <div class="clear-both"></div>

<div class="col-xs-8 col-xs-offset-2 text-left table-responsive">
    <table class="table table-bordered">
        <tr><td>topic:</td><td>  <input type="text" class="form-control topic" placeholder="topic"></td></tr>
        <tr><td>writeQueueNum:</td><td>  <input type="text" class="form-control writeQueueNum"
        placeholder="writeQueueNum"></td></tr>
        <tr><td>readQueueNum:</td><td>  <input type="text" class="form-control readQueueNum"
        placeholder="readQueueNum"></td></tr>
        <tr><td>brokerAddress:</td><td>  <input type="text" class="form-control brokerAddress"
        placeholder="brokerAddress"></td></tr>
        <tr><td>clusterName:</td><td>  <input type="text" class="form-control clusterName"
        placeholder="clusterName"></td></tr>
        <tr><td>permission:</td><td>  <input type="text" class="form-control permission"
                placeholder="permission"></td></tr>
        <tr><td>unit:</td><td>  <input type="text" class="form-control unit"
                placeholder="unit"></td></tr>
        <tr><td>hasUnitSubscription:</td><td>  <input type="text" class="form-control hasUnitSubscription"
                placeholder="hasUnitSubscription"></td></tr>
        <tr><td>order:</td><td>  <input type="text" class="form-control order" placeholder="order"></td></tr>
        <tr><td colspan="2">
    <div class="col-xs-2">
      <button type="submit" class="btn btn-primary addTopic">Add</button>
    </div>
        </td></tr>
    </table>
</div>

</body>
</html>
