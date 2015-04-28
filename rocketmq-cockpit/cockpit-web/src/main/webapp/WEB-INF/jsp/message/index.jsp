<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Query Message</title>
  <%@include file="../include/base-path.jsp"%>
  <base href="<%=basePath%>%">
  <link rel="shortcut icon" href="favicon.ico" />
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <script src="http://libs.baidu.com/jquery/1.7.0/jquery.js"></script>
  <script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
  <link href="http://libs.baidu.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet">
  <script src="js/message.js" type="application/javascript"></script>
</head>
<body>
<div class="container-fluid">
  <div class="row">
    <div class="col-md-8 col-md-offset-2 text-center">
      <h1>Query Message</h1>
    </div>
  </div>

  <select id="queryType">
    <option value="0">Query Message By ID</option>
    <option value="1">Query Message By KEY</option>
  </select>

<br />
<br />
  <div class="clear-both"></div>
  <div id="queryID" style="display:block">

    <div class="col-xs-4 col-xs-offset-2">
      <input type="text" class="form-control msgId" placeholder="message id">
    </div>

    <div class="col-xs-2">
      <button type="submit" class="btn btn-primary queryByID">query</button>
    </div>

<br />
    <br />
    <div class="row">
      <div class="col-xs-8 col-xs-offset-2 text-left table-responsive">
        <table class="table table-condense table-hover table-bordered">
          <tbody class="table-striped itable-content">
          </tbody>
        </table>

        <div class="clear-both"></div>

      </div>
    </div>
  </div>

  <div id="queryKEY" style="display:none">

    <div class="col-xs-4 col-xs-offset-2">
      <input type="text" class="form-control msgTopic" placeholder="Message Topic">
    </div>

    <div class="col-xs-4">
      <input type="text" class="form-control msgKey" placeholder="message key">
    </div>

    <div class="col-xs-2">
      <button type="submit" class="btn btn-primary queryByKEY">query</button>
    </div>

<br />
    <br />
    <div class="row">
      <div class="col-xs-8 col-xs-offset-2 text-left table-responsive">
        <table class="table table-condense table-hover table-bordered">
          <thead>
          <tr>
            <td>Message ID</td>
            <td>Tag</td>
            <td>Key</td>
            <td>Storetime</td>
            <td>Operation</td>
          </tr>
          </thead>
          <tbody class="table-striped ktable-content">
          </tbody>
        </table>

        <div class="clear-both"></div>

      </div>
    </div>
  </div>

  <div id="flow" style="display:none">
    <div class="row">
      <div class="col-xs-8 col-xs-offset-2 text-left table-responsive">
        <table class="table table-condense table-hover table-bordered">
          <tbody class="table-striped ftable-content">
          </tbody>
        </table>

        <div class="clear-both"></div>

      </div>
    </div>
  </div>
</div>
</body>
</html>
