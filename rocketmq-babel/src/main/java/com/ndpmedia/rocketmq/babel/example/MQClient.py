from thrift import Thrift
from thrift.transport import TSocket, TTransport
from thrift.protocol import TBinaryProtocol
from mq import Producer, Consumer #mq＝rocketmq lib package
import time
import aparser
import logging
from threading import Thread
from datetime import *

__author__ = 'penuel'

logger = logging.getLogger('mq')


class ProducerClient:
    def __init__(self, server_ip, server_port):
        self.__server_ip = server_ip
        self.__server_port = server_port
        try:
            self.__transport = TSocket.TSocket(server_ip, server_port)
            self.__transport = TTransport.TBufferedTransport(self.__transport)
            protocol = TBinaryProtocol.TBinaryProtocol(self.__transport)
            self.__client = Producer.Client(protocol)
            self.__transport.open()
        except Thrift.TException, e:
            print 'init : %s' % e
            logger.error(e, "ProducerClient.init")
            self.__transport.close()


    def send(self, message):
        try:
            if self.__client:
                self.__client.send(message)
                logger.debug("producerClient send msg %s", message)
        except Thrift.TException, e:
            logger.error(e)


    def close(self):
        try:
            self.__transport.close()
            logger.info("producerClient close %s", self)
        except Thrift.TException, e:
            logger.error(e)
            self.__transport.close()


    def test_send(self):
        try:
            start = time.time()
            if self.__client:
                i = 0
                while i < 1000000:
                    msg = Producer.Message('T_parser', 0, None, 'python_test %s' % i)
                    print 'test_send:python_test %s' % i
                    self.__client.send(msg)
                    i += 1
                    if i % 10000 == 0:
                        time.sleep(10)
            logger.info('ProducerClient testSend costs %d s', time.time() - start)
        except Thrift.TException, e:
            print 'test send : %s' % e
            # self.__client.stop()


class ConsumerClient(Thread):
    def __init__(self, server_ip, server_port):
        Thread.__init__(self)
        self.__server_ip = server_ip
        self.__server_port = server_port
        self.__stop = False
        try:
            self.__transport = TSocket.TSocket(server_ip, server_port)
            self.__transport = TTransport.TBufferedTransport(self.__transport)
            protocol = TBinaryProtocol.TBinaryProtocol(self.__transport)
            self.__client = Consumer.Client(protocol)
            self.__transport.open()
        except Thrift.TException, e:
            logger.error(e)
            self.__transport.close()


    def pull(self):
        try:
            if self.__client:
                result = self.__client.pull()
                if result and len(result) > 0:
                    logger.info('ConsumerClient Success Pull Result:%s', len(result))
                    for message in result:
                        if message and message.data:
                            aparser.wsgi.msg_process.parse(message.data)
                return result
        except Thrift.TException, e:
            logger.error(e)

    def run(self):
        logger.info('ConsumerClient start to lisenter [%s:%s]', self.__server_ip, self.__server_port)
        while not self.__stop:
            try:
                self.pull()
            except Exception, e:
                logger.error(e)
                if self.__transport:
                    self.__transport.close()

    def stop(self):
        self.__stop = True
        try:
            self.__transport.close()
        except Exception, e:
            logger.error('force kill ConsumerClient %s', e)
            if self.__transport:
                self.__transport.close()


if __name__ == '__main__':
    print 'MQ start', datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    # test aparser.wsgi.msg_process.parse()
    # ConsumerClient('localhost', 10922).start()
    producer_client = ProducerClient('localhost', 10921)
    producer_client.test_send()
    print 'MQ end', datetime.now().strftime('%Y-%m-%d %H:%M:%S')