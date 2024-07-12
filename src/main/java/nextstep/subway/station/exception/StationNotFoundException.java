package nextstep.subway.station.exception;

import nextstep.subway.support.error.ApiException;
import nextstep.subway.support.error.ErrorCode;

public class StationNotFoundException extends ApiException {
  public StationNotFoundException(Object data) {
    super(ErrorCode.NOT_FOUND, data);
  }
}
