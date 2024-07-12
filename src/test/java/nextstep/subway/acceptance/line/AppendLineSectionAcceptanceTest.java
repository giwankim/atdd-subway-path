package nextstep.subway.acceptance.line;

import static nextstep.subway.acceptance.line.steps.AppendLineSectionSteps.*;
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
class AppendLineSectionAcceptanceTest extends AcceptanceTest {
  @Autowired private StationRepository stationRepository;
  @Autowired private LineRepository lineRepository;

  private Station 강남역;
  private Station 역삼역;
  private Station 선릉역;
  private Station 판교역;
  private Line 이호선;

  /** Given 지하철역과 노선 생성을 하고 */
  @Override
  @BeforeEach
  protected void setUp() {
    super.setUp();
    강남역 = stationRepository.save(강남역());
    역삼역 = stationRepository.save(역삼역());
    선릉역 = stationRepository.save(선릉역());
    판교역 = stationRepository.save(판교역());
    이호선 = lineRepository.save(aLine().lineSections(new LineSections(강남_역삼_구간())).build());
  }

  /** When 구간 등록을 하면 Then 해당 노선 조회 시 등록한 구간의 하행역이 노선의 하행 종점역이다. */
  @DisplayName("지하철 구간을 등록한다.")
  @Test
  void appendLineSection() {
    LineSection 역삼_선릉_구간 =
        LineSection.builder().upStation(역삼역).downStation(선릉역).distance(20).build();

    ExtractableResponse<Response> response = 노선_구간_등록_요청(이호선, 역삼_선릉_구간);

    노선_마지막_구간으로_등록됨(response, 이호선, 역삼_선릉_구간);
  }

  /** Given 새로운 구간의 상행역이 노선에 등록되어있는 하행 종점역이 아니고 When 구간 등록을 하면 Then 400 Bad Request 에러가 반환된다. */
  @DisplayName("노선을 연장할 수 없는 구간을 등록 시 에러가 발생한다.")
  @Test
  void appendLineSectionNotAppendable() {
    LineSection disjointedSection =
        LineSection.builder().upStation(선릉역).downStation(판교역).distance(20).build();

    ExtractableResponse<Response> response = 노선_구간_등록_요청(이호선, disjointedSection);

    노선_구간_요청_실패함(response);
  }

  /** Given 구간의 하행 역이 이미 해당 노선에 등록되어 있으면 When 구간 등록을 하면 Then 400 Bad Request 에러가 반환된다. */
  @DisplayName("구간의 하행 역이 이미 해당 노선에 등록되어 있으면 구간 등록 시 에러가 발생한다.")
  @Test
  void appendLineSectionCycle() {
    LineSection cyclicSection =
        LineSection.builder().upStation(역삼역).downStation(강남역).distance(20).build();

    ExtractableResponse<Response> response = 노선_구간_등록_요청(이호선, cyclicSection);

    노선_구간_요청_실패함(response);
  }
}
