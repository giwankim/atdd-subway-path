package nextstep.subway.path.domain;

import lombok.ToString;
import nextstep.subway.line.domain.LineSection;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

@ToString
public class SubwayGraph {
  public static final String STATION_NOT_FOUND = "추가하는 구간의 상/하행역이 존재하지 않습니다.";

  private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

  public SubwayGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
    this.graph = graph;
  }

  public SubwayGraph() {
    this(new WeightedMultigraph<>(DefaultWeightedEdge.class));
  }

  public void addStation(Station station) {
    graph.addVertex(station);
  }

  public void addLineSection(LineSection lineSection) {
    Station upStation = lineSection.getUpStation();
    Station downStation = lineSection.getDownStation();
    validate(upStation, downStation);
    DefaultWeightedEdge edge = graph.addEdge(upStation, downStation);
    graph.setEdgeWeight(edge, lineSection.getDistance());
  }

  private void validate(Station upStation, Station downStation) {
    if (!graph.containsVertex(upStation) || !graph.containsVertex(downStation)) {
      throw new IllegalArgumentException(STATION_NOT_FOUND);
    }
  }

  public Path getShortestPath(Station source, Station sink) {
    validate(source, sink);
    DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath =
        new DijkstraShortestPath<>(graph);
    GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(source, sink);
    if (path == null) {
      return Path.empty();
    }
    return Path.of(path.getVertexList(), (long) path.getWeight());
  }

  public boolean isSame(SubwayGraph that) {
    if (!graph.vertexSet().equals(that.graph.vertexSet())) {
      return false;
    }
    if (graph.edgeSet().size() != that.graph.edgeSet().size()) {
      return false;
    }
    for (DefaultWeightedEdge edge : graph.edgeSet()) {
      Station source = graph.getEdgeSource(edge);
      Station target = graph.getEdgeTarget(edge);
      DefaultWeightedEdge thatEdge = that.graph.getEdge(source, target);
      if (thatEdge == null) {
        return false;
      }
      if (Math.abs(graph.getEdgeWeight(edge) - that.graph.getEdgeWeight(thatEdge)) > 10e-7) {
        return false;
      }
    }
    return true;
  }
}
