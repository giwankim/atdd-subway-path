package nextstep.subway.unit.line.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.application.StationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LineServiceMockTest {
  @Mock private LineRepository lineRepository;
  @Mock private StationService stationService;

  @Test
  void addSection() {
    // given
    // lineRepository, stationService stub 설정을 통해 초기값 셋팅

    // when
    // lineService.addSection 호출

    // then
    // lineService.findLineById 메서드를 통해 검증
  }
}
