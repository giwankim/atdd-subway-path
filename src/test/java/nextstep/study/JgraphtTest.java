package nextstep.study;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

class JgraphtTest {
  @Test
  void getDijkstraShortestPath() {
    String source = "v3";
    String target = "v1";

    WeightedMultigraph<String, DefaultWeightedEdge> graph =
        new WeightedMultigraph<>(DefaultWeightedEdge.class);
    graph.addVertex("v1");
    graph.addVertex("v2");
    graph.addVertex("v3");
    graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
    graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
    graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

    DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath =
        new DijkstraShortestPath<>(graph);
    List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

    assertThat(shortestPath).hasSize(3);
  }

  @Test
  void getKShortestPaths() {
    String source = "v3";
    String target = "v1";

    WeightedMultigraph<String, DefaultWeightedEdge> graph =
        new WeightedMultigraph<>(DefaultWeightedEdge.class);
    graph.addVertex("v1");
    graph.addVertex("v2");
    graph.addVertex("v3");
    graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
    graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
    graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

    List<GraphPath<String, DefaultWeightedEdge>> paths =
        new KShortestPaths<>(graph, 100).getPaths(source, target);

    assertThat(paths).hasSize(2);
    paths.forEach(
        it -> {
          assertThat(it.getVertexList()).startsWith(source);
          assertThat(it.getVertexList()).endsWith(target);
        });
  }
}
