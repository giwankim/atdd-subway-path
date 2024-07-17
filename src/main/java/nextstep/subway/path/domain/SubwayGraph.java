package nextstep.subway.path.domain;

import nextstep.subway.line.domain.LineSection;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph {
  private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

  public SubwayGraph() {
    this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
  }

  public void addStation(Station station) {
    graph.addVertex(station);
  }

  public boolean contains(Station station) {
    return graph.vertexSet().contains(station);
  }

  public void addLineSection(LineSection lineSection) {
    DefaultWeightedEdge edge =
        graph.addEdge(lineSection.getUpStation(), lineSection.getDownStation());
    graph.setEdgeWeight(edge, lineSection.getDistance());
  }

  public boolean containsLineSection(LineSection lineSection) {
    DefaultWeightedEdge edge =
        graph.getEdge(lineSection.getUpStation(), lineSection.getDownStation());
    if (edge == null) {
      return false;
    }
    return graph.getEdgeWeight(edge) == lineSection.getDistance();
  }

  public SubwayPath getShortestPath(Station source, Station target) {
    DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath =
        new DijkstraShortestPath<>(graph);
    GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(source, target);
    return SubwayPath.of(path.getVertexList(), (long) path.getWeight());
  }
}
