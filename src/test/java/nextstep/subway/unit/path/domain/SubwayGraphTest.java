package nextstep.subway.unit.path.domain;

import static nextstep.subway.support.Fixtures.*;
import static org.assertj.core.api.Assertions.*;

import nextstep.subway.line.domain.LineSection;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 그래프 단위 테스트")
class SubwayGraphTest {
  private final Station 교대역 = 교대역();
  private final Station 강남역 = 강남역();
  private final Station 남부터미널역 = 남부터미널역();
  private final Station 양재역 = 양재역();

  @DisplayName("역을 추가한다.")
  @Test
  void addStation() {
    SubwayGraph graph = new SubwayGraph();

    graph.addStation(교대역);

    assertThat(graph.isSame(new SubwayGraph())).isFalse();
  }

  @DisplayName("중복해서 역을 추가해도 그래프는 변하지 않는다.")
  @Test
  void duplicateAddStation() {
    SubwayGraph graph = new SubwayGraph();
    graph.addStation(교대역);

    graph.addStation(교대역);

    assertThat(
            graph.isSame(
                new SubwayGraph(
                    WeightedMultigraph.<Station, DefaultWeightedEdge>builder(
                            DefaultWeightedEdge.class)
                        .addVertex(교대역)
                        .build())))
        .isTrue();
  }

  @DisplayName("구간을 추가한다.")
  @Test
  void addLineSection() {
    SubwayGraph graph = new SubwayGraph();
    graph.addStation(교대역);
    graph.addStation(강남역);

    graph.addLineSection(LineSection.of(교대역, 강남역, 10));

    assertThat(graph.isSame(new SubwayGraph())).isFalse();
  }

  @DisplayName("Multigraph 이기에 중복해서 구간을 추가할 수 있다.")
  @Test
  void duplicateAddLineSection() {
    SubwayGraph graph = new SubwayGraph();
    graph.addStation(교대역);
    graph.addStation(강남역);
    graph.addLineSection(LineSection.of(교대역, 강남역, 10));

    graph.addLineSection(LineSection.of(교대역, 강남역, 10));

    assertThat(
        graph.isSame(
            new SubwayGraph(
                WeightedMultigraph.<Station, DefaultWeightedEdge>builder(
                        DefaultWeightedEdge.class)
                    .addEdge(교대역, 강남역, 10)
                    .addEdge(교대역, 강남역, 10)
                    .build())))
        .isTrue();
  }

  @DisplayName("추가하는 구간의 상/하행역이 존재하지 않을 때 예외를 던진다.")
  @Test
  void addLineSectionShouldThrowExceptionWhenStationNotExist() {
    SubwayGraph graph = new SubwayGraph();
    LineSection section = LineSection.of(교대역, 강남역, 10);
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> graph.addLineSection(section));
  }

  @DisplayName("최단 거리 경로를 조회한다.")
  @Test
  void getShortestPath() {
    SubwayGraph graph = new SubwayGraph();
    graph.addStation(교대역);
    graph.addStation(강남역);
    graph.addStation(양재역);
    graph.addStation(남부터미널역);
    graph.addLineSection(LineSection.of(교대역, 강남역, 10));
    graph.addLineSection(LineSection.of(강남역, 양재역, 10));
    graph.addLineSection(LineSection.of(교대역, 남부터미널역, 2));
    graph.addLineSection(LineSection.of(남부터미널역, 양재역, 3));

    Path path = graph.getShortestPath(교대역, 양재역);

    assertThat(path.getTotalDistance()).isEqualTo(5);
    assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
  }
}
