package com.zqksk.api.stock.define.indecies;

import java.util.List;

public interface Indecies {

    Double calculate(List<Double> closes);
}
