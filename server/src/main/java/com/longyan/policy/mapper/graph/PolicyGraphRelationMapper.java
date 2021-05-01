package com.longyan.policy.mapper.graph;

import com.longyan.policy.domain.graph.GraphRelation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface PolicyGraphRelationMapper extends Neo4jRepository<GraphRelation, Long> {

    @Query("match(n:GraphEntity{name:{name}})-[r:GraphRelation]->(m) return r.od")
    List<String> getEntityRelations(String name);
}
