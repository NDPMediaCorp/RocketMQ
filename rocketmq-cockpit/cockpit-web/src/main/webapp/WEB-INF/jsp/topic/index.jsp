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

<div id="addTopicDIV" class="col-xs-8 col-xs-offset-2 text-left table-responsive">
    <table class="table table-bordered">
        <tr><td>Topic:</td><td>  <input type="text" class="form-control topic" placeholder="topic"></td></tr>
        <tr class="hide"><td>Write Queue Number:</td><td>  <input type="text" class="form-control writeQueueNum"
        placeholder="Write Queue Number, Default 16"></td></tr>
        <tr class="hide"><td>Read Queue Number:</td><td>  <input type="text" class="form-control readQueueNum"
        placeholder="Read Queue Number, Default 16"></td></tr>
        <tr class="hide"><td>Broker Address:</td><td>  <input type="text" class="form-control brokerAddress"
        placeholder="IP:10911"></td></tr>
        <tr class="hide"><td>Cluster Name:</td><td>  <input type="text" class="form-control clusterName"
        placeholder="Cluster Name" value="DefaultCluster"></td></tr>
        <tr class="hide"><td>Permission:</td><td>  <input type="text" class="form-control permission"
                placeholder="Read: 2/Write: 4/Read&Write: 6"></td></tr>
        <tr class="hide"><td>Unit:</td><td>  <input type="text" class="form-control unit"
                placeholder="true/false"></td></tr>
        <tr class="hide"><td>Has Unit Subscription:</td><td>  <input type="text" class="form-control hasUnitSubscription"
                placeholder="true/false"></td></tr>
        <tr><td>Order:</td><td>  <input type="text" class="form-control order" placeholder="order"></td></tr>
        <tr><td colspan="2">
    <div class="col-xs-2">
      <button type="submit" class="btn btn-primary addTopic">Add</button>
    </div>
        </td></tr>
    </table>
</div>

<div id="sendMessageTestDIV" class="col-xs-8 col-xs-offset-2 text-left table-responsive">
    <table class="table table-bordered">
        <tr><td>Topic:</td><td>  <input type="text" class="form-control send_topic" placeholder="topic"></td></tr>
        <tr><td>Producer Group:</td><td>  <input type="text" class="form-control send_producerGroup"
        placeholder="Producer Group"></td></tr>
        <tr><td>tag:</td><td>  <input type="text" class="form-control send_tag" placeholder="tag"></td></tr>
        <tr><td>key:</td><td>  <input type="text" class="form-control send_key" placeholder="key"></td></tr>
        <tr><td>Message Body:</td><td>  <input type="text" class="form-control send_body" placeholder=""
        value="test context"></td></tr>
        <tr><td colspan="2">
    <div class="col-xs-2">
      <button type="submit" class="btn btn-primary send">send</button>
    </div>
        </td></tr>
    </table>
</div>

<button id="addButton" onclick="addButton();">Add</button>
      <table cellspacing="0" class="display" id="topic" width="100%">
        <thead>
           <tr>
                  <th>Topic</th>
                  <th>Cluster Name</th>
                  <th>Broker Address</th>
                  <th>Write Queue Number</th>
                  <th>Read Queue Number</th>
                  <th>Permission</th>
                  <th>Unit</th>
                  <th>Has Unit Subscription</th>
                  <th>Order</th>
                  <th>Status</th>
                  <th>Create Time</th>
                  <th>Update Time</th>
                  <th>Operation</th>
            </tr>
        </thead>
        <tbody>
        </tbody>
      </table>


</div>
</body>
</html>
