package nextstep.subway.unit.line.domain;

import static nextstep.subway.support.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.LineSection;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class LineSectionTest {
  private final Station 강남역 = 강남역();
  private final Station 역삼역 = 역삼역();
  private final Station 선릉역 = 선릉역();
  private final Station 판교역 = 판교역();

  @DisplayName("구간 앞에 새로운 구간을 추가할 수 있는지 검증한다.")
  @Test
  void canPrepend() {
    LineSection section = new LineSection(역삼역, 선릉역, 10);
    assertThat(section.canPrepend(new LineSection(강남역, 역삼역, 20))).isTrue();
    assertThat(section.canPrepend(new LineSection(강남역, 판교역, 30))).isFalse();
  }

  @DisplayName("구간 뒤에 새로운 구간을 추가할 수 있는지 검증한다.")
  @Test
  void canAppend() {
    LineSection section = new LineSection(강남역, 역삼역, 10);
    assertThat(section.canAppend(new LineSection(역삼역, 선릉역, 20))).isTrue();
    assertThat(section.canAppend(new LineSection(강남역, 판교역, 30))).isFalse();
  }

  @DisplayName("노선 가운데 새로운 구간을 추가할 수 있는 경우를 검증한다.")
  @Test
  void canInsertShouldReturnTrue() {
    LineSection section = new LineSection(강남역, 선릉역, 30);
    assertThat(section.canInsert(new LineSection(강남역, 역삼역, 10))).isTrue();
    assertThat(section.canInsert(new LineSection(역삼역, 선릉역, 20))).isTrue();
  }

  @DisplayName("노선 가운데 새로운 구간을 추가할 수 없는 경우를 검증한다.")
  @Test
  void canInsertShouldReturnFalse() {
    LineSection section = new LineSection(강남역, 선릉역, 30);
    SoftAssertions.assertSoftly(softAssertions -> {
      softAssertions.assertThat(section.canInsert(new LineSection(강남역, 역삼역, 40))).isFalse();
      softAssertions.assertThat(section.canInsert(new LineSection(선릉역, 판교역, 50))).isFalse();
      softAssertions.assertThat(section.canInsert(new LineSection(역삼역, 판교역, 20))).isFalse();
    });
  }
}
