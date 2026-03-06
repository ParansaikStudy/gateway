package com.zqksk.api.datasource.customer;

import com.zqksk.api.domain.CustomerDetail;
import com.zqksk.api.domain.CustomerDetailRepository;
import com.zqksk.api.exception.JpaErrorType;
import com.zqksk.api.support.exception.CoreException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerDetailCoreRepository implements CustomerDetailRepository {
    private final CustomerDetailJpaRepository customerDetailJpaRepository;

    public CustomerDetailCoreRepository(CustomerDetailJpaRepository customerDetailJpaRepository) {
        this.customerDetailJpaRepository = customerDetailJpaRepository;
    }

    @Override
    public CustomerDetail findByCustomerId(Long customerId) {
        return customerDetailJpaRepository.findByCustomerId(customerId)
                .orElseThrow(
                        () -> new CoreException(JpaErrorType.NOT_FOUND_DATA)
                ).toCustomerDetail();
    }

    @Override
    public List<CustomerDetail> findAllByCustomerId(List<Long> customerIds) {
        return customerDetailJpaRepository.findAllByCustomerIdIn(customerIds).stream()
                .map(CustomerDetailEntity::toCustomerDetail)
                .toList();
    }
}
