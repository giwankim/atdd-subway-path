package nextstep.subway.unit.path.domain;

import static nextstep.subway.support.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.LineSection;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.domain.SubwayPath;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 그래프 단위 테스트")
class SubwayGraphTest {
  private final Station 교대역 = 교대역();
  private final Station 강남역 = 강남역();
  private final Station 남부터미널역 = 남부터미널역();
  private final Station 양재역 = 양재역();
  private final Station 역삼역 = 역삼역();

  @DisplayName("지하철 역을 그래프에 추가한다.")
  @Test
  void addStation() {
    SubwayGraph graph = new SubwayGraph();

    graph.addStation(강남역);

    assertThat(graph.contains(강남역)).isTrue();
  }

  @DisplayName("지하철 역이 그래프에 포함되어 있는지 확인한다.")
  @Test
  void contains() {
    SubwayGraph graph = new SubwayGraph();

    graph.addStation(역삼역);

    assertThat(graph.contains(역삼역)).isTrue();
    assertThat(graph.contains(강남역)).isFalse();
  }

  @DisplayName("지하철 구간을 그래프에 추가한다.")
  @Test
  void addLineSection() {
    SubwayGraph graph = new SubwayGraph();
    graph.addStation(강남역);
    graph.addStation(역삼역);
    LineSection lineSection = new LineSection(강남역, 역삼역, 10);

    graph.addLineSection(lineSection);

    assertThat(graph.containsLineSection(lineSection)).isTrue();
  }

  @DisplayName("지하철 구간이 그래프에 포함되어 있는지 확인한다.")
  @Test
  void containsLineSection() {
    SubwayGraph graph = new SubwayGraph();
    graph.addStation(강남역);
    graph.addStation(역삼역);
    LineSection section = LineSection.of(강남역, 역삼역, 10);
    graph.addLineSection(section);

    assertThat(graph.containsLineSection(section)).isTrue();
  }

  @DisplayName("구간의 거리가 다른 경우에 그래프에 포함되어 있지 않다고 답변한다.")
  @Test
  void containsLineSectionShouldReturnFalseWhenDistancesAreDifferent() {
    SubwayGraph graph = new SubwayGraph();
    graph.addStation(강남역);
    graph.addStation(역삼역);
    graph.addLineSection(LineSection.of(강남역, 역삼역, 10));

    assertThat(graph.containsLineSection(LineSection.of(강남역, 역삼역, 20))).isFalse();
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

    SubwayPath path = graph.getShortestPath(교대역, 양재역);

    assertThat(path.getTotalDistance()).isEqualTo(5);
    assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
  }
}
