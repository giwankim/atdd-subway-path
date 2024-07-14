package nextstep.subway.unit.line.domain;

import static nextstep.subway.support.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Arrays;
import nextstep.subway.line.domain.LineSection;
import nextstep.subway.line.domain.LineSections;
import nextstep.subway.line.exception.CannotAddLineSectionException;
import nextstep.subway.line.exception.LineSectionAlreadyExistsException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("구간 단위 테스트")
class LineSectionsTest {
  private final Station 강남역 = 강남역();
  private final Station 역삼역 = 역삼역();
  private final Station 선릉역 = 선릉역();

  @DisplayName("노선에 구간이 없는 경우 새 구간을 등록한다.")
  @Test
  void shouldAddWhenEmpty() {
    LineSections sections = new LineSections();
    LineSection section = LineSection.of(강남역, 역삼역, 10);

    sections.add(section);

    assertThat(sections.size()).isEqualTo(1);
    assertThat(sections.getFirst().isSame(section)).isTrue();
  }

  @DisplayName("기존 구간 뒤에 새로운 구간을 추가한다.")
  @Test
  void addShouldAppend() {
    LineSections sections = new LineSections(강남역, 역삼역, 10);
    LineSection section = LineSection.of(역삼역, 선릉역, 20);

    sections.add(section);

    assertThat(sections.size()).isEqualTo(2);
    assertThat(sections.getLast().isSame(section)).isTrue();
  }

  @DisplayName("기존 구간 뒤에 새로운 구간을 추가할 때 이미 등록되어 있는 역은 등록될 수 없다.")
  @Test
  void appendShouldThrowCycleException() {
    LineSections sections =
        new LineSections(Arrays.asList(LineSection.of(강남역, 역삼역, 10), LineSection.of(역삼역, 선릉역, 20)));
    LineSection section = LineSection.of(선릉역, 역삼역, 30);
    assertThatExceptionOfType(LineSectionAlreadyExistsException.class)
        .isThrownBy(() -> sections.add(section));
  }

  @DisplayName("기존 구간 앞에 새로운 구간을 추가한다.")
  @Test
  void addShouldPrepend() {
    LineSections sections = new LineSections(역삼역, 선릉역, 20);
    LineSection section = LineSection.of(강남역, 역삼역, 10);

    sections.add(section);

    assertThat(sections.size()).isEqualTo(2);
    assertThat(sections.getFirst().isSame(section)).isTrue();
  }

  @DisplayName("기존 구간 앞에 새로운 구간을 추가할 때 이미 등록된 있는 역은 등록될 수 없다.")
  @Test
  void prependShouldThrowCycleException() {
    LineSections sections =
        new LineSections(Arrays.asList(LineSection.of(강남역, 역삼역, 10), LineSection.of(역삼역, 선릉역, 20)));
    LineSection section = LineSection.of(역삼역, 강남역, 30);
    assertThatExceptionOfType(LineSectionAlreadyExistsException.class)
        .isThrownBy(() -> sections.add(section));
  }

  @DisplayName("상행역이 같은 구간을 추가하는 경우 가운데 하행역이 추가된다.")
  @Test
  void addShouldInsertWhenUpStationsAreTheSame() {
    LineSections sections = new LineSections(강남역, 선릉역, 30);
    LineSection section = LineSection.of(강남역, 역삼역, 10);

    sections.add(section);

    assertThat(sections.size()).isEqualTo(2);
    assertThat(sections.getFirst().isSame(section)).isTrue();
    assertThat(sections.getLast().isSame(LineSection.of(역삼역, 선릉역, 20))).isTrue();
  }

  @DisplayName("상행역이 같은 구간을 추가하는 경우 구간 길이가 이전 구간 길이보다 길거나 같으면 예외 처리된다.")
  @ParameterizedTest
  @ValueSource(ints = {10, 20})
  void addShouldNotInsertWhenUpStationsAreTheSameButDistanceTooLong(int distance) {
    LineSections sections = new LineSections(강남역, 선릉역, 10);
    LineSection section = LineSection.of(강남역, 역삼역, distance);
    assertThatExceptionOfType(CannotAddLineSectionException.class)
        .isThrownBy(() -> sections.add(section));
  }

  @DisplayName("하행역이 같은 구간을 추가하는 경우 가운데 상행역이 추가된다.")
  @Test
  void addShouldInsertWhenDownStationsAreTheSame() {
    LineSections sections = new LineSections(강남역, 선릉역, 30);
    LineSection section = LineSection.of(역삼역, 선릉역, 20);

    sections.add(section);

    assertThat(sections.size()).isEqualTo(2);
    assertThat(sections.getFirst().isSame(LineSection.of(강남역, 역삼역, 10))).isTrue();
    assertThat(sections.getLast().isSame(section)).isTrue();
  }

  @DisplayName("하행역이 같은 구간을 추가하는 경우 구간 길이가 이전 구간 길이보다 길거나 같으면 예외 처리된다.")
  @ParameterizedTest
  @ValueSource(ints = {10, 20})
  void addShouldNotInsertWhenDownStationsAreTheSameButDistanceTooLong(int distance) {
    LineSections sections = new LineSections(강남역, 선릉역, 10);
    LineSection section = LineSection.of(역삼역, 선릉역, distance);
    assertThatExceptionOfType(CannotAddLineSectionException.class)
        .isThrownBy(() -> sections.add(section));
  }
}
