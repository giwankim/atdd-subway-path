package nextstep.subway.line.domain;

import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.line.exception.CycleNotAllowedException;
import nextstep.subway.line.exception.LineSectionNotAppendableException;
import nextstep.subway.line.exception.RemoveLastLineSectionException;
import nextstep.subway.line.exception.RemoveNonTerminalStationException;
import nextstep.subway.station.domain.Station;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineSections {
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "line_id")
  private final List<LineSection> sections = new ArrayList<>();

  @Builder
  public LineSections(List<LineSection> lineSections) {
    this.sections.addAll(lineSections);
  }

  public LineSections(LineSection... lineSections) {
    this.sections.addAll(Arrays.asList(lineSections));
  }

  public LineSections(Station upStation, Station downStation, int distance) {
    this(LineSection.of(upStation, downStation, distance));
  }

  public static LineSections of(Station upStation, Station downStation, int distance) {
    return new LineSections(upStation, downStation, distance);
  }

  public int size() {
    return sections.size();
  }

  public boolean isEmpty() {
    return sections.isEmpty();
  }

  public LineSection getFirst() {
    return sections.get(0);
  }

  public LineSection getLast() {
    return sections.get(sections.size() - 1);
  }

  public void add(LineSection lineSection) {
    if (isEmpty()) {
      sections.add(lineSection);
      return;
    }
    if (getFirst().canPrepend(lineSection)) {
      add(lineSection, 0, lineSection.getUpStation());
      return;
    }
    if (getLast().canAppend(lineSection)) {
      add(lineSection, sections.size(), lineSection.getDownStation());
      return;
    }
    throw new LineSectionNotAppendableException();
  }

  private void add(LineSection lineSection, int index, Station station) {
    validateAddResultInCycle(station);
    sections.add(index, lineSection);
  }

  private void validateAddResultInCycle(Station stationToAdd) {
    if (getStations().stream().anyMatch(station -> station.isSame(stationToAdd))) {
      throw new CycleNotAllowedException();
    }
  }

  public void addAll(LineSections lineSections) {
    lineSections.sections.forEach(this::add);
  }

  public List<Station> getStations() {
    if (sections.isEmpty()) {
      return Collections.emptyList();
    }
    List<Station> stations =
        sections.stream().map(LineSection::getUpStation).collect(Collectors.toList());
    stations.add(getLast().getDownStation());
    return Collections.unmodifiableList(stations);
  }

  public void removeLast(Station station) {
    validateRemove(station);
    sections.remove(sections.size() - 1);
  }

  private void validateRemove(Station station) {
    if (!isTerminalStation(station)) {
      throw new RemoveNonTerminalStationException();
    }
    if (size() <= 1) {
      throw new RemoveLastLineSectionException();
    }
  }

  private boolean isTerminalStation(Station station) {
    return getLast().getDownStation().isSame(station);
  }
}
