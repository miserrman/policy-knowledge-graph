package com.longyan.policy.domain.graph;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.data.neo4j.core.schema.Id;

import java.util.Set;

@NodeEntity(label = "GraphEntity")
public class GraphEntity {
    @Id
    private Long id;

    @Property
    private String label;

    @Property(name = "article_id")
    private String article_id;

    @Property
    private String name;

    @Relationship(type = "GraphRelation", direction = Relationship.INCOMING)
    private Set<GraphRelation> graphRelationList;

    public Set<GraphRelation> getGraphRelationList() {
        return graphRelationList;
    }

    public void setGraphRelationList(Set<GraphRelation> graphRelationList) {
        this.graphRelationList = graphRelationList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
