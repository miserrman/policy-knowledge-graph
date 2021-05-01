struct com.longyan.policy.thrift.com.longyan.policy.thrift.com.longyan.policy.thrift.PolicyTitle {
    1: i32 id,
    2: string title,
    3: string depart,
    4: string date,
    5: string link
}

struct com.longyan.policy.thrift.com.longyan.policy.thrift.com.longyan.policy.thrift.PolicyContent {
    1: i32 policy_id,
    2: string content,
    3: string condition
}

struct com.longyan.policy.thrift.com.longyan.policy.thrift.com.longyan.policy.thrift.Policy {
    1: i32 id,
    2: com.longyan.policy.thrift.com.longyan.policy.thrift.com.longyan.policy.thrift.PolicyTitle policyTitle,
    3: com.longyan.policy.thrift.com.longyan.policy.thrift.com.longyan.policy.thrift.PolicyContent policyContent
}

service com.longyan.policy.thrift.com.longyan.policy.thrift.com.longyan.policy.thrift.KnowledgeGraphPyService {
    com.longyan.policy.thrift.com.longyan.policy.thrift.com.longyan.policy.thrift.Policy spiderNewPolicy(),
    com.longyan.policy.thrift.com.longyan.policy.thrift.com.longyan.policy.thrift.Policy analysisArticleContent(1: i32 departId, 2: string title, 3: string content)
}