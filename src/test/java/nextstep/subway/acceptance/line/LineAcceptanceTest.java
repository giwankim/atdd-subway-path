package nextstep.subway.acceptance.line;

import static nextstep.subway.acceptance.line.LineAcceptanceSteps.*;
import static nextstep.subway.support.Fixtures.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Collections;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
  @Autowired private StationRepository stationRepository;

  /** Given 지하철역 생성을 하고 */
  @BeforeEach
  protected void setUp() {
    super.setUp();
    stationRepository.save(gangnam());
    stationRepository.save(yeoksam());
    stationRepository.save(seolleung());
    stationRepository.save(pangyo());
  }

  /** Given 새로운 지하철 노선 정보를 입력하고, When 관리자가 노선을 생성하면, Then 해당 노선이 생성되고 노선 목록에 포함된다. */
  @DisplayName("지하철 노선을 생성한다.")
  @Test
  void createLine() {
    ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineTwo());

    지하철_노선_생성됨(response);
    지하철_노선_목록에_포함됨(지하철_노선_목록_조회_요청(), Collections.singletonList(response));
  }

  /** Given 여러 개의 지하철 노선이 등록되어 있고, When 관리자가 지하철 노선 목록을 조회하면, Then 모든 지하철 노선 목록이 반환된다. */
  @DisplayName("지하철 노선 목록을 조회한다.")
  @Test
  void showLines() {
    ExtractableResponse<Response> createLineTwoResponse = 지하철_노선_생성_요청(lineTwo());
    ExtractableResponse<Response> createShinbundangResponse = 지하철_노선_생성_요청(shinbundang());

    ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

    지하철_노선_목록에_포함됨(response, Arrays.asList(createLineTwoResponse, createShinbundangResponse));
  }

  /** Given: 특정 지하철 노선이 등록되어 있고, When: 관리자가 해당 노선을 조회하면, Then: 해당 노선의 정보가 반환된다. */
  @DisplayName("지하철 노선을 조회한다.")
  @Test
  void showLine() {
    Line line = lineTwo();
    ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(line);
    String uri = createResponse.header(HttpHeaders.LOCATION);

    ExtractableResponse<Response> response = 지하철_노선_조회_요청(uri);

    지하철_노선_조회됨(response, line);
  }

  /** Given: 특정 지하철 노선이 등록되어 있고, When: 관리자가 해당 노선을 수정하면, Then: 해당 노선의 정보가 수정된다. */
  @DisplayName("지하철 노선을 수정한다.")
  @Test
  void updateLine() {
    String newName = "다른분당선";
    String newColor = "bg-orange-600";
    ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(shinbundang());
    String uri = createResponse.header(HttpHeaders.LOCATION);

    지하철_노선_수정_요청(uri, newName, newColor);

    지하철_노선_수정됨(uri, newName, newColor);
  }

  /** Given: 특정 지하철 노선이 등록되어 있고, When: 관리자가 해당 노선을 삭제하면, Then: 해당 노선이 삭제되고 노선 목록에서 제외된다. */
  @DisplayName("지하철 노선을 삭제한다.")
  @Test
  void deleteLine() {
    ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(lineTwo());
    String uri = createResponse.header(HttpHeaders.LOCATION);

    ExtractableResponse<Response> response = 지하철_삭제_요청(uri);

    지하철_노선_삭제됨(uri, response);
  }
}
