package com.example.productservice.event;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockResultEvent {
    String orderId;
    Boolean isSuccess;
    String failReason;
}
