package com.longyan.policy.dao;

import com.longyan.policy.domain.Enterprise;
import com.longyan.policy.domain.EnterpriseUser;
import com.longyan.policy.domain.graph.GraphEntity;
import com.longyan.policy.domain.graph.GraphRelation;
import com.longyan.policy.mapper.graph.*;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class GraphDao {

    @Autowired
    private Driver driver;

    @Autowired
    private PolicyGraphEntityMapper policyGraphEntityMapper;

    @Autowired
    private PolicyGraphRelationMapper policyGraphRelationMapper;

    private final String[] graphEntityLabel = {"Depart", "Location", "Entity", "Event", "Organization", "Person"};

    public List<String> getAllLabels() {
        return Arrays.asList(graphEntityLabel);
    }

    public HashMap<String, Object> getAllGraph() {
        List<GraphEntity> graphEntityList = policyGraphEntityMapper.getAllGraph();
        List<GraphRelation> graphRelations = new ArrayList<>();
        for (GraphEntity graphEntity : graphEntityList) {
            List<String> relations = policyGraphRelationMapper.getEntityRelations(graphEntity.getName());
            for (String r : relations) {
                GraphRelation rel = dealGraphRelation(r, graphEntityList);
                if (rel == null) {
                    continue;
                }
                graphRelations.add(rel);
            }
        }
        HashMap hashMap = new HashMap();
        hashMap.put("entity", graphEntityList);
        hashMap.put("relation", graphRelations);
        return hashMap;
    }

    public List<GraphEntity> findTupleByName(String nName, String nType, String mName, String mType) {
        String cql = "match(n:GraphEntity%s)-[]->(m%s) where n.name=~'.*%s.*' and m.name=~'.*%s.*' return n";
        if (nType != null) {
            nType = "{label:'" + nType + "'}";
        }
        else {
            nType = "";
        }
        if (mType != null) {
            mType = "{label:'" + mType + "'}";
        }
        else {
            mType = "";
        }
        cql = String.format(cql, nType, mType, nName, mName);
        List<GraphEntity> graphEntityList = executeDiffSearch(cql);
        return graphEntityList;
    }

    public List<GraphEntity> findByArticleId(Integer articleId) {
        List<GraphEntity> res = new ArrayList<>();
        String cql = String.format("match(n) where n.article_id=~'.*%s.*' return n", String.valueOf(articleId));
        List<GraphEntity> graphEntityList = executeDiffSearch(cql);
        for (GraphEntity graphEntity : graphEntityList) {
            String[] ids = graphEntity.getArticle_id().split(",");
            List<String> temp = Arrays.asList(ids);
            if (temp.contains(String.valueOf(articleId))) {
                res.add(graphEntity);
            }
        }
        return res;
    }

    public List<GraphEntity> findEntityByName(String name, String type) {
        String cql = "";
        if (type == null) {
            cql = String.format("match(n) where n.name=~'.*%s.*' return n", name);
        }
        else {
            cql = String.format("match(n:GraphEntity{label:'%s'}) where n.name=~'.*%s.*' return n", type, name);
        }
        List<GraphEntity> graphEntityList = executeDiffSearch(cql);
        return graphEntityList;
    }

    private GraphRelation dealGraphRelation(String relation, List<GraphEntity> graphEntityList) {
        List<String> graphEntityName = new ArrayList<>();
        for (GraphEntity graphEntity : graphEntityList) {
            graphEntityName.add(graphEntity.getName());
        }
        String[] s = relation.split(":");
        if (s.length >= 3) {
            GraphRelation graphRelation = new GraphRelation();
            graphRelation.setName(s[0]);
            GraphEntity start = new GraphEntity();
            start.setName(s[1]);
            GraphEntity end = new GraphEntity();
            end.setName(s[2]);
            if (!graphEntityName.contains(s[1]) || !graphEntityName.contains(s[2])) {
                return null;
            }
            graphRelation.setPolicyEntityStart(start);
            graphRelation.setGetPolicyEntityEnd(end);
            return graphRelation;
        }
        return null;
    }

    private List<GraphEntity> executeDiffSearch(String cql) {
        Session session = driver.session();
        System.out.println(cql);
        List<Map<String, Object>> res = session.run(cql)
                .list(r -> r.get("n").asNode().asMap());
        List<GraphEntity> graphEntityList = new ArrayList<>();
        for (Map<String, Object> m : res) {
            GraphEntity graphEntity = new GraphEntity();
            graphEntity.setName(String.valueOf(m.get("name")));
            graphEntity.setLabel(String.valueOf(m.get("label")));
            graphEntity.setArticle_id(String.valueOf(m.get("article_id")));
            graphEntityList.add(graphEntity);
        }
        return graphEntityList;
    }

}
