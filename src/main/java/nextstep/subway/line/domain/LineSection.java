package nextstep.subway.line.domain;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LineSection {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "up_station_id")
  private Station upStation;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "down_station_id")
  private Station downStation;

  private int distance;

  @Builder
  public LineSection(Long id, Station upStation, Station downStation, int distance) {
    this.id = id;
    this.upStation = upStation;
    this.downStation = downStation;
    this.distance = distance;
  }

  public LineSection(Station upStation, Station downStation, int distance) {
    this(null, upStation, downStation, distance);
  }

  public static LineSection of(Station upStation, Station downStation, int distance) {
    return new LineSection(upStation, downStation, distance);
  }

  public boolean canPrepend(LineSection lineSection) {
    return upStation.isSame(lineSection.downStation);
  }

  public boolean canAppend(LineSection lineSection) {
    return downStation.isSame(lineSection.upStation);
  }

  public boolean isSame(LineSection other) {
    return id.equals(other.id);
  }
}
