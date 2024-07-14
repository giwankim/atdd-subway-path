package nextstep.subway.line.exception;

import nextstep.subway.support.error.ApiException;
import nextstep.subway.support.error.ErrorCode;

public class LineSectionAlreadyExistsException extends ApiException {
  public LineSectionAlreadyExistsException() {
    super(ErrorCode.BAD_REQUEST);
  }
}
