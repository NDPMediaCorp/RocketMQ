<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Topic Management</title>
  <%@include file="../include/base-path.jsp"%>
  <base href="<%=basePath%>%">
  <link rel="shortcut icon" href="favicon.ico" />
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <script src="http://libs.baidu.com/jquery/1.7.0/jquery.js"></script>
  <script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
  <link href="http://libs.baidu.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet">
  <script src="js/consumer-group.js" type="application/javascript"></script>
</head>
<body>
<div class="container-fluid">
  <div class="row">
    <div class="col-md-8 col-md-offset-2 text-center">
      <h1>Topic Catalog</h1>
    </div>
  </div>

  <div class="clear-both"></div>

  <div class="row">
    <div class="col-xs-8 col-xs-offset-2 text-left table-responsive">
      <table class="table table-hover table-bordered">
        <thead>
        <tr>
          <td style="display:none">id</td>
          <td>cluster_name</td>
          <td style="display:none">which_broker_when_consume_slowly</td>
          <td>group_name</td>
          <td style="display:none">consume_enable</td>
          <td style="display:none">consume_broadcast_enable</td>
          <td>broker_address</td>
          <td style="display:none">broker_id</td>
          <td style="display:none">retry_max_times</td>
          <td style="display:none">retry_queue_num</td>
          <td style="display:none">consume_from_min_enable</td>
          <td style="display:none">status</td>
          <td style="display:none">create_time</td>
          <td>Operation</td>
        </tr>
        </thead>
        <tbody class="table-striped table-content">
        </tbody>
      </table>

      <div class="clear-both"></div>

    </div>
  </div>
</div>

  <div class="clear-both"></div>

<div class="col-xs-8 col-xs-offset-2 text-left table-responsive">
    <table class="table table-bordered">
        <tr><td>cluster_name:</td><td>  <input type="text" class="form-control cluster_name" placeholder="cluster_name"></td></tr>
        <tr style="display:none"><td>which_broker_when_consume_slowly:</td><td>  <input type="text" class="form-control which_broker_when_consume_slowly"
        placeholder="which_broker_when_consume_slowly"></td></tr>
        <tr><td>group_name:</td><td>  <input type="text" class="form-control group_name" placeholder="group_name"></td></tr>
        <tr><td>consume_enable:</td><td>  <input type="text" class="form-control consume_enable"
        placeholder="consume_enable"></td></tr>
        <tr><td>consume_broadcast_enable:</td><td>  <input type="text" class="form-control consume_broadcast_enable"
        placeholder="consume_broadcast_enable"></td></tr>
        <tr style="display:none"><td>broker_address:</td><td>  <input type="text" class="form-control broker_address"
                placeholder="broker_address"></td></tr>
        <tr style="display:none"><td>broker_id:</td><td>  <input type="text" class="form-control broker_id"
                placeholder="broker_id"></td></tr>
        <tr style="display:none"><td>retry_max_times:</td><td>  <input type="text" class="form-control retry_max_times"
                placeholder="retry_max_times"></td></tr>
        <tr style="display:none"><td>retry_queue_num:</td><td>  <input type="text" class="form-control retry_queue_num" placeholder="retry_queue_num"></td></tr>
        <tr style="display:none"><td>consume_from_min_enable:</td><td>  <input type="text" class="form-control consume_from_min_enable" placeholder="consume_from_min_enable"></td></tr>
        <tr><td colspan="2">
    <div class="col-xs-2">
      <button type="submit" class="btn btn-primary addConsumerGroup">Add</button>
    </div>
        </td></tr>
    </table>
</div>

</body>
</html>
