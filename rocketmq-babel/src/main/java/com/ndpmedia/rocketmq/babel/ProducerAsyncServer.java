/*
 * Copyright (c) 2015. All Rights Reserved.
 */
package com.ndpmedia.rocketmq.babel;

import com.alibaba.rocketmq.client.log.ClientLogger;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;

public class ProducerAsyncServer {

    private static final Logger LOG = ClientLogger.getLog();
    private static final int PORT = Integer.parseInt(System.getProperty("RocketMQProducerPort", "10923"));

    public static void main(String[] args) throws TTransportException {
        TNonblockingServerSocket serverSocket = new TNonblockingServerSocket(PORT);
        Producer.AsyncProcessor<Producer.AsyncIface> processor = new Producer.AsyncProcessor<Producer.AsyncIface>(new ProducerAsyncService());
        THsHaServer.Args arg = new THsHaServer.Args(serverSocket);
        arg.protocolFactory(new TCompactProtocol.Factory());
        arg.transportFactory(new TFramedTransport.Factory());
        arg.processorFactory(new TProcessorFactory(processor));

        TServer server = new THsHaServer(arg);
        server.serve();
        LOG.info("Async RocketMQProducer started.");
    }

}
