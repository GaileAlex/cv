package ee.gaile.repository.entity.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectedFilter {

    String searchArea;

    String conditionOption;

    String textRequest;

    String day;

    String month;

    String year;

}
