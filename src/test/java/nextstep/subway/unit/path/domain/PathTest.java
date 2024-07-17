package nextstep.subway.unit.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import nextstep.subway.path.domain.Leg;
import nextstep.subway.path.domain.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("경로 단위 테스트")
class PathTest {
  @DisplayName("경로의 총 거리를 계산한다.")
  @Test
  void calculateTotalDistance() {
    Path path = new Path(Leg.of(1, 2, 10), Leg.of(2, 3, 20), Leg.of(3, 4, 30));
    assertThat(path.calculateTotalDistance()).isEqualTo(60L);
  }

  @DisplayName("경로가 비어있는 경우 총 거리는 0이다.")
  @Test
  void emptyPathShouldReturnZero() {
    Path path = new Path(new ArrayList<>());
    assertThat(path.calculateTotalDistance()).isZero();
  }
}
