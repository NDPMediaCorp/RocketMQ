from thrift import Thrift
from thrift.transport import TSocket, TTransport
from thrift.protocol import TBinaryProtocol
# from core.parser.mq import Producer, Consumer
from mq import Producer, Consumer
import logging
from threading import Thread
from datetime import *
import time
mq_produce_client = []
mq_consumer_client = []

MQ_PRODUCER_IP = 'localhost'
MQ_PRODUCER_PORT = 10921
MQ_CONSUMER_IP = 'localhost'
MQ_CONSUMER_PORT = 10922


__author__ = 'penuel'

logger = logging.getLogger('aparser')


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

    @staticmethod
    def instance():
        if mq_produce_client and len(mq_produce_client) > 0:
            return mq_produce_client[0]
        try:
            # pc = ProducerClient('localhost', 10921)
            pc = ProducerClient(MQ_PRODUCER_IP, MQ_PRODUCER_PORT)
            if pc:
                mq_produce_client.append(pc)
                return mq_produce_client[0]
        except Exception, e:
            logger.error(e)

    @staticmethod
    def send(message):
        try:
            instance = ProducerClient.instance()
            if not instance:
                time.sleep(1)
                instance = ProducerClient.instance()
                if not instance:
                    time.sleep(1)
                    instance = ProducerClient.instance()
            result = instance.__client.send(message)
            logger.debug("producerClient send msg %s,result=", message, result)
            return result
        except Exception, e:
            logger.error(e)
            return ''


    @staticmethod
    def close():
        try:
            instance = ProducerClient.instance()
            if instance:
                instance.__transport.close()
            logger.info("producerClient close %s", instance)
        except Thrift.TException, e:
            logger.error(e)


    @staticmethod
    def test_send():
        try:
            i = 0
            while i < 1000000:
                msg = Producer.Message('T_parser_trace', 0, None, 'python_test %s' % i)
                result = ProducerClient.send(msg)
                print 'test_send:python_test %s,result=%s' % (i, result)
                i += 1
                if i % 10000 == 0:
                    time.sleep(10)
            logger.info('ProducerClient testSend over')
        except Thrift.TException, e:
            print 'test send : %s' % e


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
                            # TODO something
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
    ProducerClient.test_send()
    print 'MQ end', datetime.now().strftime('%Y-%m-%d %H:%M:%S')