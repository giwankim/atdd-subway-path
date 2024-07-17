package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Path {
  private final List<Leg> legs;

  public Path(List<Leg> legs) {
    this.legs = legs;
  }

  public Path(Leg... legs) {
    this.legs = Arrays.asList(legs);
  }

  public List<Leg> getLegs() {
    return Collections.unmodifiableList(legs);
  }

  public long calculateTotalDistance() {
    return legs.stream().mapToLong(Leg::getDistance).sum();
  }
}
