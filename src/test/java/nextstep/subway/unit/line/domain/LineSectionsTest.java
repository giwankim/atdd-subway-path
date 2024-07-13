package nextstep.subway.unit.line.domain;

import static nextstep.subway.support.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Arrays;
import nextstep.subway.line.domain.LineSection;
import nextstep.subway.line.domain.LineSections;
import nextstep.subway.line.exception.CycleNotAllowedException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("구간 단위 테스트")
class LineSectionsTest {
  private final Station 강남역 = 강남역();
  private final Station 역삼역 = 역삼역();
  private final Station 선릉역 = 선릉역();
  private final Station 판교역 = 판교역();

  @DisplayName("지하철 구간 등록 기능")
  @Nested
  class AddTest {
    @DisplayName("노선에 구간이 없는 경우 새 구간을 등록한다.")
    @Test
    void emptySections() {
      LineSections sections = new LineSections();
      LineSection section = LineSection.of(강남역, 역삼역, 10);

      sections.add(section);

      assertThat(sections.size()).isEqualTo(1);
      isSameSection(sections.getFirst(), section);
    }

    @DisplayName("구간 상행역이 노선 하행 종점역과 같은 경우")
    @Nested
    class AppendTest {
      @DisplayName("기존 구간 뒤에 새로운 구간을 추가한다.")
      @Test
      void append() {
        LineSections sections = new LineSections(강남역, 역삼역, 10);
        LineSection section = LineSection.of(역삼역, 선릉역, 20);

        sections.add(section);

        assertThat(sections.size()).isEqualTo(2);
        isSameSection(sections.getLast(), section);
      }

      @DisplayName("기존 구간 뒤에 새로운 구간을 추가할 때 이미 등록된 있는 역은 등록될 수 없다.")
      @Test
      void appendResultsInCycle() {
        LineSections sections =
            new LineSections(
                Arrays.asList(LineSection.of(강남역, 역삼역, 10), LineSection.of(역삼역, 선릉역, 20)));
        LineSection section = LineSection.of(선릉역, 역삼역, 30);

        assertThatExceptionOfType(CycleNotAllowedException.class)
            .isThrownBy(() -> sections.add(section));
      }
    }

    @DisplayName("구간 하행역이 노선 상행 종점역과 같은 경우")
    @Nested
    class PrependTest {
      @DisplayName("기존 구간 앞에 새로운 구간을 추가한다.")
      @Test
      void prepend() {
        LineSections sections = new LineSections(역삼역, 선릉역, 20);
        LineSection section = LineSection.of(강남역, 역삼역, 10);

        sections.add(section);

        assertThat(sections.size()).isEqualTo(2);
        isSameSection(sections.getFirst(), section);
      }

      @DisplayName("기존 구간 앞에 새로운 구간을 추가할 때 이미 등록된 있는 역은 등록될 수 없다.")
      @Test
      void prependResultsInCycle() {
        LineSections sections =
            new LineSections(
                Arrays.asList(LineSection.of(강남역, 역삼역, 10), LineSection.of(역삼역, 선릉역, 20)));
        LineSection section = LineSection.of(역삼역, 강남역, 30);

        assertThatExceptionOfType(CycleNotAllowedException.class)
            .isThrownBy(() -> sections.add(section));
      }
    }

    @DisplayName("노선 가운데 역을 추가 할 수 있는 경우")
    @Nested
    class InsertTest {
      @DisplayName("노선에 역 추가시 노선 가운데 추가 할 수 있다.")
      @Test
      void insertUpStationSame() {
        LineSections sections = new LineSections(강남역, 선릉역, 10);
        LineSection section = LineSection.of(강남역, 역삼역, 30);

        sections.add(section);

        assertThat(sections.size()).isEqualTo(2);
        isSameSection(sections.getFirst(), section);
      }
    }
  }

  private static void isSameSection(LineSection thisSection, LineSection otherSection) {
    assertThat(thisSection.getUpStation().isSame(otherSection.getUpStation())).isTrue();
    assertThat(thisSection.getDownStation().isSame(otherSection.getDownStation())).isTrue();
    assertThat(thisSection.getDistance()).isEqualTo(otherSection.getDistance());
  }
}
