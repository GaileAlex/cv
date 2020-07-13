package ee.gaile.entity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FilterWrapper {

    @JsonProperty("selectedFilter")
    private List<SelectedFilter> selectedFilters;

    /**
     * @return the Filters
     */
    public List<SelectedFilter> getFilters() {
        return selectedFilters;
    }

    /**
     * @param selectedFilters the Filters to set
     */
    public void setFilter(List<SelectedFilter> selectedFilters) {
        this.selectedFilters = selectedFilters;
    }
}
