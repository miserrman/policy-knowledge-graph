# -*- coding:utf-8 -*-
from service.ttypes import PolicyTitle
from service.ttypes import PolicyContent
from service.ttypes import Policy
from model.train_predict import AnalysisContent
from processing import dealwithTitle
import spider_new
from service.constants import PolicyDepart
from py2neo import *
from processing import dealwithTitle
import re


class KnowledgePolicyListener(object):

    def spiderNewPolicy(self):
        #返回所有页码的网址
        pageurls = spider_new.get_pages_url("https://www.fujiansme.com/index.php?m=content&c=index&a=policy_lists&catid=40&page=%s")
        #返回每篇政策的部门号,url,标题,日期
        detailurls = spider_new.addnew_perdetail_url(pageurls)
        #返回政策内容、html代码contents[i]['htmlcon']
        contents = spider_new.get_content(detailurls)

        all =[]
        for i in range(len(detailurls)):
            policyTitle = PolicyTitle()
            policyContent = PolicyContent()
            policyTitle.title = detailurls[i]['title']
            depart = detailurls[i]['type']
            policyTitle.depart = PolicyDepart.get(int(depart), "")
            policyTitle.date = detailurls[i]['deadline']
            policyTitle.link = detailurls[i]['href']
            policyContent.content = contents[i]['htmlcon']
            #提取条件
            article_content = contents[i]['content']
            paras = self.cut_sentence(article_content)
            res = []
            for para in paras:
                if re.search(r"\d+%|\d+.*元|\d+天|\d+工作日", para):
                    res.append(para)
            conditions = '|'.join(res)
            policyContent.condition = conditions
            ###
            policy = Policy()
            policy.policyTitle = policyTitle
            policy.policyContent = policyContent
            all.append(policy)
        return all

    def analysisArticleContent(self, articleId, title, content):
        vec_path = 'model/model1000/word2vec_model'
        model_path = "model/model1000"
        #处理标题
        title_head, title_back, title_key = dealwithTitle(title)
        #返回预测结果
        contentEntity, titleEntity = AnalysisContent(model_path, vec_path, title_back, content, articleId)
        print('title_head', title_head)
        print('title_back', title_back)
        print('title_key', title_key)
        print('contentEntity', contentEntity)
        print('titleEntity', titleEntity)
        kgraph = Graph(
            "http://39.102.35.144:7474",
            username="neo4j",
            password="policy-123456"
        )
        # 内容节点关系创建
        self.create_nodes_rela(kgraph, contentEntity)
        # 标题节点关系创建
        for org in title_head:
            titleEntity.insert(0, [articleId, 0, org, 'GraphEntity'])
        for key in title_key:
            titleEntity.append([articleId, 0, key, 'GraphEntity'])
        self.create_nodes_rela(kgraph, titleEntity)

        policyTitle = PolicyTitle()
        policyContent = PolicyContent()
        policyTitle.title = title
        policyContent.content = content
        policy = Policy(1, policyTitle, policyContent)
        return policy

    def create_nodes_rela(self, kgraph, contentEntity):
        nodematcher = NodeMatcher(kgraph)
        relamatcher = RelationshipMatcher(kgraph)
        for i in range(len(contentEntity)):
            entity = contentEntity[i]
            contentId = entity[0]#文章编号
            ename = entity[2]#实体名
            type = entity[3]#类型
            if len(ename) > 20:#预测过长舍去
                continue
            # 节点不存在创建，否则更改id属性
            if nodematcher.match(name=ename).first() == None:
                enode = Node('GraphEntity', name=ename, article_id = str(contentId))
                kgraph.create(enode)
            else:
                enode = nodematcher.match(name=ename).first()
                print(enode)
                print(self.ContainsArticles(enode['article_id'], contentId))
                # 判断文章编号是否已经记录在article_id中
                if self.ContainsArticles(enode['article_id'], contentId) == False:
                    enode['article_id'] = enode['article_id'] + ',' + str(contentId)
                    kgraph.push(enode)

            if i == 0 or prenode == enode:
                prenode = enode
                continue

            # 关系不存在创建，否则更改id属性
            od = '在:' + prenode['name'] + ":" + enode['name']
            if relamatcher.match([prenode,enode]).first() == None:
                rela = Relationship(prenode, 'GraphRelation', enode, article_id= str(contentId), od=od)
                kgraph.create(rela)
            else:
                rela = relamatcher.match([prenode, enode]).first()
                # 判断文章编号是否已经记录在article_id中
                if self.ContainsArticles(rela['article_id'], contentId) == False:
                    rela['article_id'] = rela['article_id'] + ',' + str(contentId)
                    rela['od'] = od
                    kgraph.push(rela)
            prenode = enode

    def ContainsArticles(self, articleIds, contentId):
        contentId = str(contentId)
        if articleIds == contentId:
            return True
        if articleIds.find(contentId+",") == 0:
            return True
        if articleIds.find(","+contentId+",") > 0:
            return True
        if articleIds[(len(articleIds)-len(contentId)-1)] == ',':
            return  True
        return False

    def cut_sentence(paragraph):
        pattern = '《|》|。|[\s]|；'
        result = re.split(pattern, paragraph)
        return result
