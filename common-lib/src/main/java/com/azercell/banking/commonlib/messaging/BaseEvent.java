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
public class BaseEvent<T> {

    private String eventId;
    private ResponseQueueInfo responseQueueInfo;
    private T payload;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseEvent<?> baseEvent = (BaseEvent<?>) o;
        return Objects.equals(getEventId(), baseEvent.getEventId())
                && Objects.equals(getResponseQueueInfo(), baseEvent.getResponseQueueInfo())
                && Objects.equals(getPayload(), baseEvent.getPayload());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEventId(), getResponseQueueInfo(), getPayload());
    }

}
