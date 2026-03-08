package com.zqksk.api.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerFinder {
    private final CustomerRepository customerRepository;

    public CustomerFinder(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerLocation> getCustomerLocationList(double neX, double neY, double swX, double swY) {
        return customerRepository.findAllByBounds(neX, neY, swX, swY);
    }

    public Customer getCustomer(String cimsCustomerCode) {
        return customerRepository.findByCimsCustomerCode(cimsCustomerCode);
    }

    public List<Customer> getCustomerList(List<String> cimsCustomerCodes) {
        return customerRepository.findAllByCimsCustomerCode(cimsCustomerCodes);
    }
}
