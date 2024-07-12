package nextstep.subway.line.exception;

import nextstep.subway.support.error.ApiException;
import nextstep.subway.support.error.ErrorCode;

public class CycleNotAllowedException extends ApiException {
  public CycleNotAllowedException() {
    super(ErrorCode.BAD_REQUEST);
  }
}
