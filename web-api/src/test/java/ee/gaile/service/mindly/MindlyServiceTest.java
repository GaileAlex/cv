package ee.gaile.service.mindly;

import ee.gaile.ApplicationIT;
import ee.gaile.entity.mindly.Mindly;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

class MindlyServiceTest extends ApplicationIT {

    @Autowired
    private MindlyService mindlyService;

    @Test
    void getAllPortfolio_savePortfolio_deletePortfolioById() {
        Mindly portfolio = new Mindly(null, "Bitcoin", new BigDecimal(1),
                LocalDate.parse("2021-01-01"), "My personal wallet", new BigDecimal(10));
        mindlyService.savePortfolio(portfolio);

        List<Mindly> mindlyList = mindlyService.getAllPortfolio();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(mindlyList.size()).isEqualTo(4);
            softly.assertThat(mindlyList.get(3).getCryptocurrency()).isEqualTo("Bitcoin");
            softly.assertThat(mindlyList.get(3).getAmount()).isEqualTo(new BigDecimal(1));
            softly.assertThat(mindlyList.get(3).getWalletLocation()).isEqualTo("My personal wallet");
        });

        mindlyService.deletePortfolioById(mindlyList.get(3).getId());
        List<Mindly> mindlyListAfterDelete = mindlyService.getAllPortfolio();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(mindlyListAfterDelete.size()).isEqualTo(3);
        });

    }

}
