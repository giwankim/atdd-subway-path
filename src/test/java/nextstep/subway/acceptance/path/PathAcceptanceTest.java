package nextstep.subway.acceptance.path;

import static nextstep.subway.acceptance.path.steps.PathAcceptanceSteps.*;
import static nextstep.subway.support.Fixtures.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineSections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("경로 조회 기능 인수테스트")
class PathAcceptanceTest extends AcceptanceTest {
  @Autowired StationRepository stationRepository;
  @Autowired LineRepository lineRepository;

  private Station 교대역;
  private Station 양재역;

  /**
   * Given 지하철역들과 노선들을 생성하고
   *
   * <pre>
   * 교대역    --- *2호선* ---   강남역
   * |                        |
   * *3호선*                   *신분당선*
   * |                        |
   * 남부터미널역  --- *3호선* ---   양재
   * </pre>
   */
  @Override
  @BeforeEach
  protected void setUp() {
    super.setUp();
    교대역 = stationRepository.save(교대역());
    stationRepository.save(강남역());
    stationRepository.save(남부터미널역());
    양재역 = stationRepository.save(양재역());
    lineRepository.save(aLine().lineSections(new LineSections(교대_강남_구간())).build());
    lineRepository.save(
        aLine()
            .id(2L)
            .name("신분당선")
            .color("bg-red-600")
            .lineSections(new LineSections(강남_양재_구간()))
            .build());
    lineRepository.save(삼호선());
  }

  /** When 출발역과 도착역으로 경로를 조회하면 Then 출발역부터 도탁역까지의 경로에 있는 역 목록이 조회된다. */
  @DisplayName("지하철 경로에 있는 역 조회")
  @Test
  void shouldReturnShortestDistancePath() {
    ExtractableResponse<Response> response = 경로_조회_요청(교대역, 양재역);
    경로_역_목록_조회됨(response, "교대역", "남부터미널역", "양재역");
  }

  /** When 출발역과 도착역으로 경로를 조회하면 Then 출발역부터 도탁역까지의 경로 구간의 거리가 조회된다. */
  @DisplayName("지하철 경로 거리 조회")
  @Test
  void shouldReturnShortestDistance() {
    ExtractableResponse<Response> response = 경로_조회_요청(교대역, 양재역);
    경로_거리_조회됨(response, 5);
  }
}
