package ee.gaile.entity.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@MappedSuperclass
public class SelectedFilter implements Serializable {
    String conditionOption;

    String day;

    String month;

    String searchArea;

    String textRequest;

    Integer year;

}
