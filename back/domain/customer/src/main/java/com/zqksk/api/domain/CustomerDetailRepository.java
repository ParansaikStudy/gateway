package com.zqksk.api.domain;

import java.util.List;

public interface CustomerDetailRepository {
    CustomerDetail findByCustomerId(Long customerId);
    List<CustomerDetail> findAllByCustomerId(List<Long> customerIds);
}
