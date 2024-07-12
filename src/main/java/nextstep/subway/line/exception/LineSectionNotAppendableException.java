package nextstep.subway.line.exception;

import nextstep.subway.support.error.ApiException;
import nextstep.subway.support.error.ErrorCode;

public class LineSectionNotAppendableException extends ApiException {
  public LineSectionNotAppendableException() {
    super(ErrorCode.LINE_SECTION_NOT_APPENDABLE);
  }
}
