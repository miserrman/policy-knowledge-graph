package com.longyan.policy.mapper.graph;

import com.longyan.policy.domain.graph.GraphEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyGraphEntityMapper extends Neo4jRepository<GraphEntity, Long> {

    @Query("match(n) return n limit 25")
    List<GraphEntity> getAllGraph();

    @Query("match(n) where n.article_id=~'{id},.*' return n")
    List<GraphEntity> findByArticleId(@Param("id") String id);

}
