package com.zqksk.api.domain;

import java.util.List;

public interface CustomerRepository {
    List<CustomerLocation> findAllByBounds(double neX, double neY, double swX, double swY);
    Customer findByCimsCustomerCode(String cimsCustomerCode);
    List<Customer> findAllByCimsCustomerCode(List<String> cimsCustomerCodes);
}
