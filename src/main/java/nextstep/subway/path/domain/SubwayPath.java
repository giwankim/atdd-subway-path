package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import nextstep.subway.station.domain.Station;

public class SubwayPath {
  private final List<Station> stations;
  @Getter private final long totalDistance;

  public SubwayPath(List<Station> stations, long totalDistance) {
    this.stations = stations;
    this.totalDistance = totalDistance;
  }

  public static SubwayPath of(List<Station> stations, long totalDistance) {
    return new SubwayPath(stations, totalDistance);
  }

  public List<Station> getStations() {
    return Collections.unmodifiableList(stations);
  }
}
