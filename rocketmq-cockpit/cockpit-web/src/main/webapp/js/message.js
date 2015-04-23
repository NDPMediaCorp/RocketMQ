
$(document).ready(function() {

    var select = document.getElementById("queryType");

    select.onchange = function (){
        var queryType = $(this).children('option:selected').val();

        if ("0" === queryType){
            document.getElementById("queryKEY").style.display = "none";
            document.getElementById("queryID").style.display = "block";
        }else{
            document.getElementById("queryID").style.display = "none";
            document.getElementById("queryKEY").style.display = "block";
        }
    } ;

    $(".queryByID").click(function() {
        var msgId = $("input.msgId").val();
        if (msgId.length != 32){
            alert(" wrong message id ");
            return;
        }
        $.ajax({
            async: false,
            url: "cockpit/api/message" + "/" + msgId,
            type: "GET",
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function(message) {
                $(".itable-content").children().remove();
                if (null != message){
                    var operationLink = $("<a class='flowItem' href='javascript:;'>flow</a>");
                    operationLink.attr("rel", message.msgId);
                    var operation = $("<td>message flow: </td>").append(operationLink);
                    var pro = message.properties;
                    var cons = getMapValue(pro);
                    var item = $("<tr><td>Message ID:" + message.msgId + "</td></tr>" + "<tr><td>Topic:" + message.topic + "</td></tr>" + "<tr><td>Tag:" + message.tags + "</td></tr>" + "<tr><td>Key:" + message.keys + "</td></tr>" + "<tr><td>Userproperties:" + cons + "</td></tr>" + "<tr><td>Borntime:" + message.bornTime + "</td></tr>" + "<tr><td>BornHost:" + message.bornHost + "</td></tr>" + "<tr><td>Storetime:" + message.storTime + "</td></tr>" + "<tr><td>StoreHost:" + message.storeHost + "</td></tr>" + "<tr><td>Messagebody:[length]" + message.body.length + "   <a href='cockpit/api/message/download/" + message.msgId + "'>download</a></td></tr>");

                    $(".itable-content").append(item);
                    $(".itable-content").append($("<tr></tr>").append(operation));
                }
            }
        });

    });

    $(".queryByKEY").click(function() {
        var topic = $("input.msgTopic").val();
        var key = $("input.msgKey").val();

        if ("" === topic || "" === key){
            alert(" please input topic and key");
            return;
        }

        $.ajax({
            async: false,
            url: "cockpit/api/message" + "/" + topic + "/" + key,
            type: "GET",
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function(backdata) {
                $(".ktable-content").children().remove();
                if (null != backdata){
                    backdata.forEach(function(message) {
                        var operationLink = $("<a class='operationItem' href='javascript:;'>detail</a>    <a class='flowItem' href='javascript:;'>flow</a>");
                        operationLink.attr("rel", message.msgId);
                        var operation = $("<td></td>").append(operationLink);
                        var item = $("<tr><td>" + message.msgId + "</td><td>" + message.tags + "</td><td>" + message.keys + "</td><td>" + message.storTime + "</td></tr>");
                        item.append(operation);
                        $(".ktable-content").append(item);
                   });
                }
            }
        });

    });

    $(".operationItem").live("click", function() {
        var msgId = $(this).attr("rel");
        $("input.msgId").val(msgId);
        $.ajax({
            async: false,
            url: "cockpit/api/message" + "/" + msgId,
            type: "GET",
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function(message) {
                document.getElementById("queryKEY").style.display = "none";
                document.getElementById("queryID").style.display = "block";
                document.getElementById("queryType").options[0].selected=true;
                $(".itable-content").children().remove();
                var pro = message.properties;
                var cons = getMapValue(pro);
                var item = $("<tr><td>Message ID:" + message.msgId + "</td></tr>" + "<tr><td>Topic:" + message.topic + "</td></tr>" + "<tr><td>Tag:" + message.tags + "</td></tr>" + "<tr><td>Key:" + message.keys + "</td></tr>" + "<tr><td>Userproperties:" + cons + "</td></tr>" + "<tr><td>Borntime:" + message.bornTime + "</td></tr>" + "<tr><td>BornHost:" + message.bornHost + "</td></tr>" + "<tr><td>Storetime:" + message.storTime + "</td></tr>" + "<tr><td>StoreHost:" + message.storeHost + "</td></tr>" + "<tr><td>Messagebody:[length]" + message.body.length + "   <a href='cockpit/api/message/download/" + message.msgId + "'>download</a></td></tr><tr><td>message flow: <a class='flowItem' href='javascript:;'>flow</a> </td></tr>");
                $(".itable-content").append(item);
            },
            error: function() {
                alert("Error");
            }
        });
    });

    $(".flowItem").live("click", function() {
        var msgId = $(this).attr("rel");
        $("input.msgId").val(msgId);
        $.ajax({
            async: false,
            url: "cockpit/api/message" + "/flow/" + msgId,
            type: "GET",
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            success: function(backdata) {
                document.getElementById("flow").style.display = "block";
                $(".ftable-content").children().remove();
                if (null != backdata){
                    backdata.forEach(function(cockpitMessageFlow) {
                        var item = $("<tr><td>tracer:" + cockpitMessageFlow.tracerId + "</td><td>source:" + cockpitMessageFlow.source + "</td><td>status:" + cockpitMessageFlow.status + "</td><td>time:" + new Date(cockpitMessageFlow.timeStamp) + "</td></tr>");

                        $(".ftable-content").append(item);
                   });
                }
            },
            error: function() {
                alert("Error");
            }
        });
    });

});

function getMapValue(sourceMap){
    var cons = "{";
    var index = 0;
    for (var i in sourceMap){
        if(index++ > 0){
            cons = cons + ","
        }
        cons = cons + i + "=" + sourceMap[i];
    }
    cons = cons + "}";
    return cons;
}