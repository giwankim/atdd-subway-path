package nextstep.subway.unit.line.domain;

import static nextstep.subway.support.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.LineSection;
import nextstep.subway.line.domain.LineSections;
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
      LineSection firstSection = sections.getFirst();
      assertThat(firstSection.getUpStation().isSame(section.getUpStation())).isTrue();
      assertThat(firstSection.getDownStation().isSame(section.getDownStation())).isTrue();
      assertThat(firstSection.getDistance()).isEqualTo(section.getDistance());
    }

    @DisplayName("기존 구간 뒤에 새로운 구간을 추가한다.")
    @Test
    void append() {
      LineSections sections = new LineSections(강남역, 역삼역, 10);
      LineSection section = LineSection.of(역삼역, 선릉역, 20);

      sections.add(section);

      assertThat(sections.size()).isEqualTo(2);
      LineSection lastSection = sections.getLast();
      assertThat(lastSection.getUpStation().isSame(section.getUpStation())).isTrue();
      assertThat(lastSection.getDownStation().isSame(section.getDownStation())).isTrue();
      assertThat(lastSection.getDistance()).isEqualTo(section.getDistance());
    }

    @DisplayName("기존 구간 앞에 새로운 구간을 추가한다.")
    @Test
    void prepend() {
      LineSections sections = new LineSections(역삼역, 선릉역, 20);
      LineSection section = LineSection.of(강남역, 역삼역, 10);

      sections.add(section);

      assertThat(sections.size()).isEqualTo(2);
      LineSection firstSection = sections.getFirst();
      assertThat(firstSection.getUpStation().isSame(section.getUpStation())).isTrue();
      assertThat(firstSection.getDownStation().isSame(section.getDownStation())).isTrue();
      assertThat(firstSection.getDistance()).isEqualTo(section.getDistance());
    }
  }
}
