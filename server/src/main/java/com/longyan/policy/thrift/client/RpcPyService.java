package com.longyan.policy.thrift.client;

import com.longyan.policy.thrift.KnowledgeGraphPyService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.longyan.policy.thrift.Policy;
import java.util.List;

@Service
public class RpcPyService {

    TTransport transport = new TSocket("1.116.205.128", 9000);
    private KnowledgeGraphPyService.Client getClient() {
        TProtocol tProtocol = new TBinaryProtocol(transport);
        KnowledgeGraphPyService.Client client = new KnowledgeGraphPyService.Client(tProtocol);
        return client;
    }

    public List<Policy> spiderRecentlyPolicy() {
        KnowledgeGraphPyService.Client client = getClient();
        try {
            transport.open();
            List<Policy> policyList  = client.spiderNewPolicy();
            System.out.println(policyList.get(0).getPolicyContent().content);
            transport.close();
            return policyList;
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Async
    public Policy analysisPolicy(Integer articleId, String policyTitle, String policyContent) {
        KnowledgeGraphPyService.Client client = getClient();
        try {
            transport.open();
            Policy policy = client.analysisArticleContent(articleId, policyTitle, policyContent);
            transport.close();
            return policy;
        }catch (TTransportException e) {
            transport.close();
            e.printStackTrace();
        }catch (TException e) {
            transport.close();
            e.printStackTrace();
        }
        return null;
    }
}
