package nextstep.subway.acceptance.line;

import static nextstep.subway.acceptance.line.steps.LineSectionAcceptanceSteps.*;
import static nextstep.subway.support.Fixtures.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineSections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("지하철 구간 제거 기능")
class RemoveLineSectionAcceptanceTest extends AcceptanceTest {
  @Autowired StationRepository stationRepository;
  @Autowired LineRepository lineRepository;

  private Station 역삼역;
  private Station 선릉역;
  private Line 이호선;

  /** Given 지하철역과 노선 생성을 요청 하고 */
  @Override
  @BeforeEach
  protected void setUp() {
    super.setUp();
    stationRepository.save(강남역());
    역삼역 = stationRepository.save(역삼역());
    선릉역 = stationRepository.save(선릉역());
    이호선 =
        lineRepository.save(
            aLine().id(1L).lineSections(new LineSections(강남_역삼_구간(), 역삼_선릉_구간())).build());
  }

  /** Given 구간이 하나 이상 등록되어 있고 When 종점역 구간 제거를 요청하면 Then 해당 노선 조회 시 등록한 구간의 하행 종점역에서 제외된다. */
  @DisplayName("지하철 구간을 제거한다.")
  @Test
  void removeLineSection() {
    ExtractableResponse<Response> response = 노선_구간_삭제_요청(이호선, 선릉역);
    노선_구간_삭제됨(response, 이호선, 선릉역);
  }

  /** Given 구간이 하나 이상 등록되어 있고 When 하행 종점역이 아닌 구간을 삭제 요청하면 Then 400 Bad Request 에러가 반환된다. */
  @DisplayName("최종 종점 역이 아닌 구간 제거 시 에러가 발생한다.")
  @Test
  void removeNonTerminalLineSectionShouldReturnError() {
    ExtractableResponse<Response> response = 노선_구간_삭제_요청(이호선, 역삼역);
    노선_구간_삭제_실패함(response);
  }

  /** Given 노선의 구간이 1개인 경우 When 구간을 삭제 요청하면 Then 400 Bad Request 에러가 반환된다. */
  @DisplayName("구간이 하나만 존재할 때 구간 제거 시 에러가 발생한다.")
  @Test
  void removeLastLineSectionShouldReturnError() {
    노선_구간_삭제_요청(이호선, 선릉역);

    ExtractableResponse<Response> response = 노선_구간_삭제_요청(이호선, 역삼역);

    노선_구간_삭제_실패함(response);
  }
}
