from KnowledgePolicyListener import KnowledgePolicyListener
from service import KnowledgeGraphPyService
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer

_HOST = "0.0.0.0"
_PORT = 9000

if __name__ == "__main__":
    handler = KnowledgePolicyListener()
    processor = KnowledgeGraphPyService.Processor(handler)
    transport = TSocket.TServerSocket(_HOST, _PORT)
    tFactory = TTransport.TBufferedTransportFactory()
    pFactory = TBinaryProtocol.TBinaryProtocolFactory()
    rpcServer = TServer.TSimpleServer(processor, transport, tFactory, pFactory)
    rpcServer.serve()


