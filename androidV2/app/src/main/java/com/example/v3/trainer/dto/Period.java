package com.example.v3.trainer.dto;

import java.io.Serializable;

public class Period implements Serializable {
    private String ex_period;

    public Period(String ex_period) {
        this.ex_period = ex_period;
    }

    public String getEx_period() {
        return ex_period;
    }

    public void setEx_period(String ex_period) {
        this.ex_period = ex_period;
    }
}
