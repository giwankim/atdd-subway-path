package nextstep.subway.station.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StationService {
  private StationRepository stationRepository;

  public StationService(StationRepository stationRepository) {
    this.stationRepository = stationRepository;
  }

  @Transactional
  public StationResponse saveStation(StationRequest stationRequest) {
    Station station = stationRepository.save(new Station(stationRequest.getName()));
    return createStationResponse(station);
  }

  public List<StationResponse> findAllStations() {
    return stationRepository.findAll().stream()
        .map(this::createStationResponse)
        .collect(Collectors.toList());
  }

  @Transactional
  public void deleteStationById(Long id) {
    stationRepository.deleteById(id);
  }

  private StationResponse createStationResponse(Station station) {
    return new StationResponse(station.getId(), station.getName());
  }
}
