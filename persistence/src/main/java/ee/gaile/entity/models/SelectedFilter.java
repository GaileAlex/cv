package ee.gaile.entity.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectedFilter implements Serializable {
    String conditionOption;

    String day;

    String month;

    String searchArea;

    String textRequest;

    String year;

}
