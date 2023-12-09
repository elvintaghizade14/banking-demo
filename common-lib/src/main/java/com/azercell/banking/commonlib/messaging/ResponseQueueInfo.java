package com.azercell.banking.commonlib.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseQueueInfo {

    private String exchangeName;
    private String routingKey;

}
