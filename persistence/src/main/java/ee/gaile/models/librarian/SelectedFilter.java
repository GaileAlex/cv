package ee.gaile.models.librarian;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectedFilter {

    String conditionOption;
    String day;
    String month;
    String searchArea;
    String textRequest;
    String year;

}
