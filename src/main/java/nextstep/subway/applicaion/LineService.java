package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.common.AddTypeEnum;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        if (request.getUpStationId() != null && request.getDownStationId() != null && request.getDistance() != 0) {
            Station upStation = stationService.findById(request.getUpStationId());
            Station downStation = stationService.findById(request.getDownStationId());
            line.addSection(AddTypeEnum.BACK_ADD_SECTION, Section.builder()
                    .line(line)
                    .upStation(upStation)
                    .downStation(downStation)
                    .distance(request.getDistance())
                    .build());
        }
        return createLineResponse(line);
    }

    public List<LineResponse> showLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findById(Long id) {
        return createLineResponse(lineRepository.findById(id).orElseThrow(IllegalArgumentException::new));
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        if (lineRequest.getName() != null) {
            line.setName(lineRequest.getName());
        }
        if (lineRequest.getColor() != null) {
            line.setColor(lineRequest.getColor());
        }
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addSection(AddTypeEnum addTypeEnum, Long lineId, SectionRequest sectionRequest) {
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());
        Line line = lineRepository.findById(lineId).orElseThrow(IllegalArgumentException::new);

        line.addSection(addTypeEnum, Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(sectionRequest.getDistance())
                .build());
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                createStationResponses(line)
        );
    }

    private List<StationResponse> createStationResponses(Line line) {
        if (line.getSections().isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = line.getStations();

        return stations.stream()
                .map(it -> stationService.createStationResponse(it))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(IllegalArgumentException::new);
        Station deleteStation = stationService.findById(stationId);

        line.removeSection(deleteStation);
    }
}
