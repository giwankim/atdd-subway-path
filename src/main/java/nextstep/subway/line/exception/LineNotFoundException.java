package nextstep.subway.line.exception;

import nextstep.subway.support.error.ApiException;
import nextstep.subway.support.error.ErrorCode;

public class LineNotFoundException extends ApiException {
  public LineNotFoundException(Long id) {
    super(ErrorCode.NOT_FOUND, "노선 #" + id + "이 존재하지 않습니다.");
  }
}
