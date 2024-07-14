package nextstep.subway.support.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  DEFAULT(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러가 발생했습니다."),
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
  NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 정보입니다."),
  CANNOT_ADD_LINE_SECTION(HttpStatus.BAD_REQUEST, "추가할 수 없는 구간입니다."),
  LINE_SECTION_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 해당 노선에 구간이 등록되어 있습니다."),
  REMOVE_NON_TERMINAL_STATION(HttpStatus.BAD_REQUEST, "노선의 하행 종점역만 제거할 수 있습니다."),
  REMOVE_LAST_LINE_SECTION(HttpStatus.BAD_REQUEST, "노선에 구간이 1개인 경우 역을 삭제할 수 없습니다.");

  private final HttpStatus status;
  private final String message;
}
