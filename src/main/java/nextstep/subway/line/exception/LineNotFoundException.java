package nextstep.subway.line.exception;

import nextstep.subway.support.error.ApiException;
import nextstep.subway.support.error.ErrorCode;

public class LineNotFoundException extends ApiException {
  public LineNotFoundException(Object data) {
    super(ErrorCode.NOT_FOUND, data);
  }
}
