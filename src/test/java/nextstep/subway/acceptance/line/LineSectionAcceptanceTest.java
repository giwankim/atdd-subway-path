package nextstep.subway.acceptance.line;

import static nextstep.subway.acceptance.line.steps.LineSectionAcceptanceSteps.*;
import static nextstep.subway.support.Fixtures.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineSection;
import nextstep.subway.line.domain.LineSections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 구간 등록 기능")
class LineSectionAcceptanceTest extends AcceptanceTest {
  @Autowired private StationRepository stationRepository;
  @Autowired private LineRepository lineRepository;

  private Station 강남역;
  private Station 역삼역;
  private Station 선릉역;
  private Line 이호선;

  /** Given 지하철역과 노선 생성을 하고 */
  @Override
  @BeforeEach
  protected void setUp() {
    super.setUp();
    강남역 = stationRepository.save(강남역());
    역삼역 = stationRepository.save(역삼역());
    선릉역 = stationRepository.save(선릉역());
  }

  /** Given 구간의 상행역이 등록되어 있지 않고 하행역이 노선의 상행 종점역과 같은 경우 When 구간 등록을 하면 Then 노선 조회시 해당 구간이 첫 구간이다 */
  @DisplayName("구간이 노선 첫 구간으로 등록된다.")
  @Test
  void shouldPrependLineSection() {
    LineSections 역삼_선릉_구간 = LineSections.of(역삼역, 선릉역, 10);
    이호선 = lineRepository.save(aLine().lineSections(역삼_선릉_구간).build());
    LineSection 강남_역삼_구간 = LineSection.of(강남역, 역삼역, 20);

    ExtractableResponse<Response> response = 노선_구간_등록_요청(이호선, 강남_역삼_구간);

    노선_첫_구간으로_등록됨(response, 이호선, 강남_역삼_구간);
  }

  /** Given 구간의 하행역이 등록되어 있지 않고 상행역이 노선의 하행 종점역과 같은 경우 When 구간 등록을 하면 Then 노선 조회시 해당 구간이 마지막 구간이다 */
  @DisplayName("구간이 노선 마지막 구간으로 등록된다.")
  @Test
  void shouldAppendLineSection() {
    LineSections 강남_역삼_구간 = LineSections.of(강남역, 역삼역, 10);
    이호선 = lineRepository.save(aLine().lineSections(강남_역삼_구간).build());
    LineSection 역삼_선릉_구간 = LineSection.of(역삼역, 선릉역, 20);

    ExtractableResponse<Response> response = 노선_구간_등록_요청(이호선, 역삼_선릉_구간);

    노선_마지막_구간으로_등록됨(response, 이호선, 역삼_선릉_구간);
  }

  /** Given 새로운 구간의 상행역이 노선에 등록되어있는 하행 종점역이 아니고 When 구간 등록을 하면 Then 400 Bad Request 에러가 반환된다. */
  @DisplayName("구간이 노선 중간에 등록된다.")
  @Test
  void shouldAddLineSectionToMiddle() {
    LineSections 강남_선릉_구간 = LineSections.of(강남역, 선릉역, 30);
    이호선 = lineRepository.save(aLine().lineSections(강남_선릉_구간).build());
    LineSection 강남_역삼_구간 = LineSection.of(강남역, 역삼역, 10);

    ExtractableResponse<Response> response = 노선_구간_등록_요청(이호선, 강남_역삼_구간);

    노선_i변째_구간으로_등록됨(response, 이호선, 강남_역삼_구간, 1);
  }
}
