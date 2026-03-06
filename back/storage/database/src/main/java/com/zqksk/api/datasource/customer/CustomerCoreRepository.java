package com.zqksk.api.datasource.customer;

import com.zqksk.api.domain.Customer;
import com.zqksk.api.domain.CustomerLocation;
import com.zqksk.api.domain.CustomerRepository;
import com.zqksk.api.domain.CustomerStatus;
import com.zqksk.api.exception.JpaErrorType;
import com.zqksk.api.support.exception.CoreException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerCoreRepository implements CustomerRepository {
    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerDetailJpaRepository customerDetailJpaRepository;

    public CustomerCoreRepository(CustomerJpaRepository customerJpaRepository, CustomerDetailJpaRepository customerDetailJpaRepository) {
        this.customerJpaRepository = customerJpaRepository;
        this.customerDetailJpaRepository = customerDetailJpaRepository;
    }

    @Override
    public List<CustomerLocation> findAllByBounds(double neX, double neY, double swX, double swY) {
        return customerJpaRepository.findAllByLongitudeBetweenAndLatitudeBetweenAndStatusCodeIn(swX, neX, swY, neY, CustomerStatus.ACTIVE.getStatusCodes()).stream()
                .map(CustomerEntity::toCustomerLocation)
                .toList();
    }

    @Override
    public Customer findByCimsCustomerCode(String cimsCustomerCode) {
        return customerJpaRepository.findByCimsCustomerCode(cimsCustomerCode)
                .orElseThrow(
                        () -> new CoreException(JpaErrorType.NOT_FOUND_DATA, "Customer not found.")
                ).toCustomer();
    }

    @Override
    public List<Customer> findAllByCimsCustomerCode(List<String> cimsCustomerCodes) {
        return customerJpaRepository.findAllByCimsCustomerCodeIn(cimsCustomerCodes).stream()
                .map(CustomerEntity::toCustomer)
                .toList();
    }
}
