package nextstep.subway.support;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Line.LineBuilder;
import nextstep.subway.line.domain.LineSection;
import nextstep.subway.line.domain.LineSections;
import nextstep.subway.station.domain.Station;

public class Fixtures {
  private Fixtures() {}

  public static Station gangnam() {
    return Station.builder().id(1L).name("강남역").build();
  }

  public static Station yeoksam() {
    return Station.builder().id(2L).name("역삼역").build();
  }

  public static Station seolleung() {
    return Station.builder().id(3L).name("선릉역").build();
  }

  public static Station pangyo() {
    return Station.builder().id(4L).name("판교역").build();
  }

  public static LineBuilder aLine() {
    return Line.builder().id(1L).name("2호선").color("bg-green-600");
  }

  public static Line lineTwo() {
    return Line.builder()
        .id(1L)
        .name("2호선")
        .color("bg-green-600")
        .lineSections(new LineSections(gangnamToYeoksam()))
        .build();
  }

  public static Line shinbundang() {
    return Line.builder()
        .id(2L)
        .name("신분당선")
        .color("bg-red-600")
        .lineSections(new LineSections(gangnamToPangyo()))
        .build();
  }

  public static LineSection gangnamToYeoksam() {
    return LineSection.builder()
        .id(1L)
        .upStation(gangnam())
        .downStation(yeoksam())
        .distance(10)
        .build();
  }

  public static LineSection yeoksamToSeolleung() {
    return LineSection.builder()
        .id(2L)
        .upStation(yeoksam())
        .downStation(seolleung())
        .distance(20)
        .build();
  }

  public static LineSection gangnamToPangyo() {
    return LineSection.builder()
        .id(3L)
        .upStation(gangnam())
        .downStation(pangyo())
        .distance(20)
        .build();
  }
}
