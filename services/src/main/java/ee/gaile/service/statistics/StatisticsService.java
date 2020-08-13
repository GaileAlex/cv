package ee.gaile.service.statistics;

import ee.gaile.entity.statistics.VisitStatisticGraph;
import ee.gaile.entity.statistics.VisitStatistics;
import ee.gaile.entity.statistics.VisitStatisticsDTO;
import ee.gaile.repository.VisitStatisticsRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class StatisticsService {
    private final VisitStatisticsRepository visitStatisticsRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public void setUserStatistics(HttpServletRequest request) {
        Optional<VisitStatistics> visitStatistics =
                visitStatisticsRepository.findByUserIP(request.getHeader("userIP"));

        if (visitStatistics.isPresent()) {
            visitStatistics.get().setLastVisit(new Date());
            visitStatistics.get().setTotalVisits(visitStatistics.get().getTotalVisits() + 1);
            if (visitStatistics.get().getUsername() == null || visitStatistics.get().getUsername().equals("undefined")
                    && request.getHeader("user") != null) {
                visitStatistics.get().setUsername(request.getHeader("user"));
            }
        } else {
            VisitStatistics visitStatistic = VisitStatistics.builder()
                    .username(request.getHeader("user"))
                    .lastVisit(new Date())
                    .firstVisit(new Date())
                    .totalVisits(1L)
                    .userIP(request.getHeader("userIP"))
                    .userLocation(request.getHeader("userCountry"))
                    .build();

            visitStatisticsRepository.save(visitStatistic);
        }
    }

    public VisitStatisticGraph getStatisticsGraph() {
        List<VisitStatistics> visitStatisticsList = visitStatisticsRepository.findAll();
        List<Date> visitStatisticUserDTOList = new ArrayList<>();

        visitStatisticsList.forEach((c) -> c.getVisitStatisticUsers().forEach((v) -> {
            Date dateVisit = v.getVisitDate();
            visitStatisticUserDTOList.add(dateVisit);
        }));

        Map<Date, Long> countedVisit = visitStatisticUserDTOList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        List<VisitStatisticsDTO> visitStatisticsDTOList = new ArrayList<>();
        visitStatisticsList.forEach((c) -> {
            visitStatisticsDTOList.add(toDto(c));
        });

        return new VisitStatisticGraph(visitStatisticsDTOList, countedVisit);
    }

    private VisitStatisticsDTO toDto(VisitStatistics visitStatistics) {
        return modelMapper.map(visitStatistics, VisitStatisticsDTO.class);
    }
}
