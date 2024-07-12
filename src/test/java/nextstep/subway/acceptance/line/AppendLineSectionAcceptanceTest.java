package nextstep.subway.acceptance.line;

import static nextstep.subway.acceptance.line.AppendLineSectionSteps.*;
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

@DisplayName("지하철 구간 등록 기능")
class AppendLineSectionAcceptanceTest extends AcceptanceTest {
  @Autowired private StationRepository stationRepository;
  @Autowired private LineRepository lineRepository;

  private Station gangnam;
  private Station yeoksam;
  private Station seolleung;
  private Station pangyo;
  private Line lineTwo;

  /** Given 지하철역과 노선 생성을 하고 */
  @Override
  @BeforeEach
  protected void setUp() {
    super.setUp();
    gangnam = stationRepository.save(gangnam());
    yeoksam = stationRepository.save(yeoksam());
    seolleung = stationRepository.save(seolleung());
    pangyo = stationRepository.save(pangyo());
    lineTwo =
        lineRepository.save(aLine().lineSections(new LineSections(gangnamToYeoksam())).build());
  }

  /** When 구간 등록을 하면 Then 해당 노선 조회 시 등록한 구간의 하행역이 노선의 하행 종점역이다. */
  @DisplayName("지하철 구간을 등록한다.")
  @Test
  void appendLineSection() {
    LineSection yeoksamToSeolleung =
        LineSection.builder().upStation(yeoksam).downStation(seolleung).distance(20).build();

    ExtractableResponse<Response> response = 노선_구간_등록_요청(lineTwo, yeoksamToSeolleung);

    노선_구간_등록됨(response, lineTwo, yeoksamToSeolleung);
  }

  /** Given 새로운 구간의 상행역이 노선에 등록되어있는 하행 종점역이 아니고 When 구간 등록을 하면 Then 400 Bad Request 에러가 반환된다. */
  @DisplayName("노선을 연장할 수 없는 구간을 등록 시 에러가 발생한다.")
  @Test
  void appendLineSectionNotAppendable() {
    LineSection disjointedSection =
        LineSection.builder().upStation(seolleung).downStation(pangyo).distance(20).build();

    ExtractableResponse<Response> response = 노선_구간_등록_요청(lineTwo, disjointedSection);

    노선_구간_요청_실패함(response);
  }

  /** Given 구간의 하행 역이 이미 해당 노선에 등록되어 있으면 When 구간 등록을 하면 Then 400 Bad Request 에러가 반환된다. */
  @DisplayName("구간의 하행 역이 이미 해당 노선에 등록되어 있으면 구간 등록 시 에러가 발생한다.")
  @Test
  void appendLineSectionCycle() {
    LineSection cyclicSection =
        LineSection.builder().upStation(yeoksam).downStation(gangnam).distance(20).build();

    ExtractableResponse<Response> response = 노선_구간_등록_요청(lineTwo, cyclicSection);

    노선_구간_요청_실패함(response);
  }
}
