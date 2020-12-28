package ee.gaile.models.librarian;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FilterWrapper {

    @JsonProperty("selectedFilter")
    private List<SelectedFilter> selectedFilters;

}
