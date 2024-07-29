package com.crio.RentRead.Entity.Stat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = StatusDeserializer.class)
public enum Status {
    AVAILABLE,
    UNAVAILABLE
}
