struct PolicyTitle {
    1: i32 id,
    2: string title,
    3: string depart,
    4: string date,
    5: string link
}

struct PolicyContent {
    1: i32 policy_id,
    2: string content,
    3: string condition
}

struct Policy {
    1: i32 id,
    2: PolicyTitle policyTitle,
    3: PolicyContent policyContent
}

service KnowledgeGraphPyService {
    list<Policy> spiderNewPolicy(),
    Policy analysisArticleContent(1: i32 articleId, 2: string title, 3: string content)
}