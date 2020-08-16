package ee.gaile.models.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitStatisticsUserDTO {

    private Date visitDate;

    private Long countVisit;

}
