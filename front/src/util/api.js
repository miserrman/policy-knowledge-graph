// let serverApi = 'http://39.102.35.144:8081/'
let serverApi = 'http://localhost:8081/'
export default {
    getAllPolicyTitle: serverApi + 'policy/title/',
    searchPolicyByTitle: serverApi + 'policy/title/search/',
    getPolicyById: serverApi + 'policy/',
    login: serverApi + 'user/',
    getUserList: serverApi + 'manage/user',
    register: serverApi + 'user/',
    getPolicyList: serverApi + 'manage/policy',
    getPolicyGraph: serverApi + "policy/graph",
    initSubscribeDialog: serverApi + "policy/subscribe",
    getSubscribePolicy: serverApi + "policy/subscribe/user",
    saveSubscribeForm: serverApi + "policy/subscribe",
    getPolicyCreateInfo: serverApi + "policy/create/info",
    publishPolicyArticle: serverApi + "policy",
    getManagePolicyList: serverApi + "policy/manage",
    managePolicy: serverApi + "policy/manage",
    getUserEnterprise: serverApi + "user/enterprise",
    getRecommendPolicyTitles: serverApi + "enterprise/recommend",
    getRelevantPolicyTitles: serverApi + "policy/{id}/relevant",
    getUserMessage: serverApi + "info/user",
}
