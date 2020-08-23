package ee.gaile.service.statistics;

import ee.gaile.entity.statistics.VisitStatistics;
import ee.gaile.models.statistics.VisitStatisticGraph;
import ee.gaile.models.statistics.VisitStatisticsDTO;
import ee.gaile.entity.statistics.VisitStatisticsUser;
import ee.gaile.repository.VisitStatisticsRepository;
import ee.gaile.repository.VisitStatisticsUserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class StatisticsService {
    private final VisitStatisticsRepository visitStatisticsRepository;
    private final VisitStatisticsUserRepository visitStatisticsUserRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public void setUserStatistics(HttpServletRequest request) {
        Optional<VisitStatistics> visitStatistics =
                visitStatisticsRepository.findByUserIP(request.getHeader("userIP"));

        if (visitStatistics.isPresent()) {
            visitStatistics.get().setLastVisit(LocalDate.now());
            visitStatistics.get().setTotalVisits(visitStatistics.get().getTotalVisits() + 1);
            if (visitStatistics.get().getUsername() == null || visitStatistics.get().getUsername().equals("undefined")
                    && request.getHeader("user") != null) {
                visitStatistics.get().setUsername(request.getHeader("user"));
            }
        } else {
            VisitStatistics visitStatistic = VisitStatistics.builder()
                    .username(request.getHeader("user"))
                    .lastVisit(LocalDate.now())
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
        List<VisitStatisticsUser> countedVisitDTOList = visitStatisticsUserRepository.selectVisitStatistic();

        List<VisitStatisticsDTO> visitStatisticsDTOList = new ArrayList<>();
        visitStatisticsList.forEach((c) -> visitStatisticsDTOList.add(toDto(c)));

        return new VisitStatisticGraph(visitStatisticsDTOList, countedVisitDTOList);
    }

    private VisitStatisticsDTO toDto(VisitStatistics visitStatistics) {
        return modelMapper.map(visitStatistics, VisitStatisticsDTO.class);
    }
}
