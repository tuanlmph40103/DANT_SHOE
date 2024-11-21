package com.dan.shoe.shoe.models.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum OrderType {
    POS,
    ONLINE,
    SHIP
}