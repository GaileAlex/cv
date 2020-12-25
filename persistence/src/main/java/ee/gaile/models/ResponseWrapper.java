package ee.gaile.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ResponseWrapper<T> {

    public ResponseWrapper(T data) {
        this.data = data;
    }

    private T data;
}
