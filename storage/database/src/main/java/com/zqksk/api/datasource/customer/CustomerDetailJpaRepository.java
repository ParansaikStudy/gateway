package com.zqksk.api.datasource.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerDetailJpaRepository extends JpaRepository<CustomerDetailEntity, Long> {
    Optional<CustomerDetailEntity> findByCustomerId(Long customerId);
    List<CustomerDetailEntity> findAllByCustomerIdIn(List<Long> customerIds);
}
