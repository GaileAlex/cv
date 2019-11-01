package ee.gaile.service.librarian;



import ee.gaile.repository.entity.models.SelectedFilter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class JsonParse {

    private final String json;

    public JsonParse(String json) {
        this.json = json;
    }

    /**
     * It processes data received from the front-end.
     *
     * @return a list of search terms
     * @throws ParseException
     */
    public List<SelectedFilter> parse() throws ParseException {

        List<SelectedFilter> selectedFilterList = new ArrayList<>();

        Object obj = new JSONParser().parse(json);

        JSONObject jo = (JSONObject) obj;

        JSONArray searchOptions = (JSONArray) jo.get("selectedFilter");

        for (Object searchOption : searchOptions) {
            JSONObject body = (JSONObject) searchOption;

            if (body.get("textRequest").toString().isEmpty() && !body.get("searchArea").toString().equals("Date")) {
                continue;
            }

            selectedFilterList.add(new SelectedFilter(
                    body.get("searchArea").toString(),
                    body.get("conditionOption").toString(),
                    body.get("textRequest").toString(),
                    body.get("day").toString(),
                    body.get("month").toString(),
                    body.get("year").toString()));
        }

        return selectedFilterList;
    }
}
