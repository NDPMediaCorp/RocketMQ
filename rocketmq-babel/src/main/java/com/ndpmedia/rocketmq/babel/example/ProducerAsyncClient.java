/*
 * Copyright (c) 2015. All Rights Reserved.
 */
package com.ndpmedia.rocketmq.babel.example;

import com.ndpmedia.rocketmq.babel.Message;
import com.ndpmedia.rocketmq.babel.Producer;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;

import java.io.IOException;

public class ProducerAsyncClient {
    private static final int PORT = Integer.parseInt(System.getProperty("RocketMQProducerPort", "10923"));

    public static void main(String[] args) throws TException, IOException, InterruptedException {
        TAsyncClientManager asyncClientManager = new TAsyncClientManager();
        TNonblockingTransport nonblockingTransport = new TNonblockingSocket("localhost", PORT, 3000);
        TProtocolFactory protocol = new TCompactProtocol.Factory();
        Producer.AsyncClient asyncClient = new Producer.AsyncClient(protocol, asyncClientManager, nonblockingTransport);
        Message message = new Message();
        message.setTopic("T_PARSER");
        int i = 0;
        while (true) {
            message.setData(("Test" + i).getBytes());
            System.out.println("producerClient send msg:"+"Test"+i);
            i++;
            asyncClient.send(message, new AsyncMethodCallback() {
                @Override
                public void onComplete(Object o) {
                    if (o instanceof Producer.AsyncClient.send_call) {
                        Producer.AsyncClient.send_call sendCall = (Producer.AsyncClient.send_call) o;
                        try {

                            /*
                             * Print msgId.
                             */
                            System.out.println(sendCall.getResult());
                        } catch (TException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });

            Thread.sleep(1000);
        }

    }
}
