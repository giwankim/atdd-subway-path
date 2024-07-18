package nextstep.subway.path.application;

import lombok.Getter;

@Getter
public class PathRequest {
  private final Long source;
  private final Long target;

  public PathRequest(Long source, Long target) {
    this.source = source;
    this.target = target;
  }

  public static PathRequest of(Long source, Long target) {
    return new PathRequest(source, target);
  }
}
