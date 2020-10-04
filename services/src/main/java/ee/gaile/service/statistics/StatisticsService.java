package ee.gaile.service.statistics;

import ee.gaile.entity.statistics.VisitStatisticUserIp;
import ee.gaile.entity.statistics.VisitStatistics;
import ee.gaile.entity.statistics.VisitStatisticsNewUser;
import ee.gaile.entity.statistics.VisitStatisticsUser;
import ee.gaile.models.statistics.VisitStatisticGraph;
import ee.gaile.models.statistics.VisitStatisticsDTO;
import ee.gaile.repository.VisitStatisticIpRepository;
import ee.gaile.repository.VisitStatisticsNewUserRepository;
import ee.gaile.repository.VisitStatisticsRepository;
import ee.gaile.repository.VisitStatisticsUserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class StatisticsService {
    private final VisitStatisticsRepository visitStatisticsRepository;
    private final VisitStatisticIpRepository visitStatisticIpRepository;
    private final VisitStatisticsUserRepository visitStatisticsUserRepository;
    private final VisitStatisticsNewUserRepository visitStatisticsNewUserRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public void setUserStatistics(HttpServletRequest request) {
        String userIP = request.getHeader("userIP");

        Optional<VisitStatistics> visitStatistics =
                visitStatisticsRepository.findByUserIP(userIP);

        if (visitStatistics.isPresent()) {
            visitStatistics.get().setLastVisit(new Date());
            visitStatistics.get().setTotalVisits(visitStatistics.get().getTotalVisits() + 1);

            VisitStatisticUserIp visitStatisticUserIp = new VisitStatisticUserIp();
            visitStatisticUserIp.setUserIp(userIP);
            visitStatisticUserIp.setVisitStatistics(visitStatistics.get());

            visitStatisticIpRepository.save(visitStatisticUserIp);

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
                    .userLocation(request.getHeader("userCountry"))
                    .userCity(request.getHeader("userCity"))
                    .build();

            visitStatisticsRepository.save(visitStatistic);

            VisitStatisticUserIp visitStatisticUserIp = new VisitStatisticUserIp();
            visitStatisticUserIp.setUserIp(userIP);
            visitStatisticUserIp.setVisitStatistics(visitStatistic);

            visitStatisticIpRepository.save(visitStatisticUserIp);
        }
    }

    public VisitStatisticGraph getStatisticsGraph() {
        List<VisitStatistics> visitStatisticsList = visitStatisticsRepository.findAll();
        List<VisitStatisticsUser> countedVisitDTOList = visitStatisticsUserRepository.selectVisitStatistic();
        List<VisitStatisticsNewUser> visitStatisticsNewUsers = visitStatisticsNewUserRepository.selectNewVisitors();

        List<VisitStatisticsDTO> visitStatisticsDTOList = new ArrayList<>();
        visitStatisticsList.forEach((c) -> visitStatisticsDTOList.add(toDto(c)));

        return new VisitStatisticGraph(visitStatisticsDTOList, countedVisitDTOList, visitStatisticsNewUsers);
    }

    private VisitStatisticsDTO toDto(VisitStatistics visitStatistics) {
        return modelMapper.map(visitStatistics, VisitStatisticsDTO.class);
    }

}
