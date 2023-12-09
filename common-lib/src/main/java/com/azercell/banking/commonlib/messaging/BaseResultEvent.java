package com.azercell.banking.commonlib.messaging;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResultEvent<T> {

    private String eventId;
    private Result result;
    private T payload;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseResultEvent<?> that = (BaseResultEvent<?>) o;
        return Objects.equals(getEventId(), that.getEventId()) && getResult() == that.getResult()
                && Objects.equals(getPayload(), that.getPayload());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEventId(), getResult(), getPayload());
    }

}
