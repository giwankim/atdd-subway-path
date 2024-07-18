package nextstep.subway.unit.path.application;

import static nextstep.subway.support.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import nextstep.subway.path.application.GraphService;
import nextstep.subway.path.application.PathRequest;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.station.application.StationReader;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("경로 조회 서비스 단위 테스트")
class PathServiceTest {
  @Mock GraphService graphService;
  @Mock StationReader stationReader;
  @InjectMocks PathService pathService;

  @DisplayName("경로를 조회한다.")
  @Test
  void findPath() {
    Station 교대역 = 교대역();
    Station 강남역 = 강남역();
    Station 남부터미널역 = 남부터미널역();
    Station 양재역 = 양재역();
    given(graphService.loadGraph())
        .willReturn(
            new SubwayGraph(
                WeightedMultigraph.<Station, DefaultWeightedEdge>builder(DefaultWeightedEdge.class)
                    .addEdge(교대역, 강남역, 10)
                    .addEdge(강남역, 양재역, 10)
                    .addEdge(교대역, 남부터미널역, 2)
                    .addEdge(남부터미널역, 양재역, 3)
                    .build()));
    given(stationReader.readById(교대역.getId())).willReturn(교대역);
    given(stationReader.readById(양재역.getId())).willReturn(양재역);

    PathRequest request = PathRequest.of(교대역.getId(), 양재역.getId());
    Path path = pathService.findPath(request);

    assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
    assertThat(path.getTotalDistance()).isEqualTo(5);
  }
}
