package com.longyan.policy.domain.graph;

import org.neo4j.ogm.annotation.*;
import org.springframework.data.neo4j.core.schema.Id;

import java.util.List;

@RelationshipEntity(type = "GraphRelation")
public class GraphRelation {
    @Id
    private Long id;

    private String name;

    private List<String> graphRelationList;

    @StartNode
    private GraphEntity graphEntityStart;

    @EndNode
    private GraphEntity getGraphEntityEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GraphEntity getPolicyEntityStart() {
        return graphEntityStart;
    }

    public void setPolicyEntityStart(GraphEntity graphEntityStart) {
        this.graphEntityStart = graphEntityStart;
    }

    public GraphEntity getGetPolicyEntityEnd() {
        return getGraphEntityEnd;
    }

    public void setGetPolicyEntityEnd(GraphEntity getGraphEntityEnd) {
        this.getGraphEntityEnd = getGraphEntityEnd;
    }

    public List<String> getGraphRelationList() {
        return graphRelationList;
    }

    public void setGraphRelationList(List<String> graphRelationList) {
        this.graphRelationList = graphRelationList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
