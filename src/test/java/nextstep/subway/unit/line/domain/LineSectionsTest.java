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
    @DisplayName("기존 구간 뒤에 새로운 구간을 추가한다.")
    @Test
    void append() {
      LineSections sections = new LineSections(강남역, 역삼역, 10);
      LineSection section = LineSection.of(역삼역, 선릉역, 20);

      sections.add(section);

      assertThat(sections.size()).isEqualTo(2);
      assertThat(sections.getLast().isSame(section)).isTrue();
    }
  }
}
