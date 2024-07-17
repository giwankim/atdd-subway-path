package nextstep.subway.path.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Leg {
  private final long start;
  private final long end;
  private final int distance;

  public static Leg of(long start, long end, int distance) {
    return new Leg(start, end, distance);
  }
}
