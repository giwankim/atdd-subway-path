package nextstep.subway.acceptance.line;

import static nextstep.subway.acceptance.line.LineAcceptanceSteps.지하철_노선_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.springframework.http.HttpStatus;

public class RemoveLineSectionSteps {
  private RemoveLineSectionSteps() {}

  public static ExtractableResponse<Response> 노선_구간_삭제_요청(Line line, Station station) {
    String uri = String.format("/lines/%d/sections", line.getId());
    return RestAssured.given()
        .log()
        .all()
        .queryParam("stationId", station.getId())
        .when()
        .delete(uri)
        .then()
        .log()
        .all()
        .extract();
  }

  public static void 노선_구간_삭제됨(ExtractableResponse<Response> response, Line line, Station station) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    ExtractableResponse<Response> lineResponse = 지하철_노선_조회_요청("/lines/" + line.getId());
    List<Long> stationIds = lineResponse.jsonPath().getList("stations.id", Long.class);
    assertThat(stationIds).isNotEmpty().doesNotContain(station.getId());
  }

  public static void 노선_구간_삭제_실패함(ExtractableResponse<Response> response) {
    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }
}
