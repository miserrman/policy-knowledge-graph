package com.longyan.policy.domain.graph;

import org.neo4j.ogm.annotation.*;
import org.springframework.data.neo4j.core.schema.Id;

@RelationshipEntity(type = "")
public class PolicyRelation {
    @Id
    private Integer id;

    @Property
    private String name;

    @StartNode
    private GraphEntity graphEntityStart;

    @EndNode
    private GraphEntity getGraphEntityEnd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
