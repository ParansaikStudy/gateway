package com.zqksk.api.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerDetailFinder {
    private final CustomerDetailRepository customerDetailRepository;

    public CustomerDetailFinder(CustomerDetailRepository customerDetailRepository) {
        this.customerDetailRepository = customerDetailRepository;
    }

    public CustomerDetail getCustomerDetail(Long customerId) {
        return customerDetailRepository.findByCustomerId(customerId);
    }

    public List<CustomerDetail> getCustomerDetailList(List<Long> customerIds) {
        return customerDetailRepository.findAllByCustomerId(customerIds);
    }
}
