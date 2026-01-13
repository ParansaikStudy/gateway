package com.zqksk.api.datasource.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {
    List<CustomerEntity> findAllByLongitudeBetweenAndLatitudeBetweenAndStatusCodeIn(double swX, double neX, double swY, double neY, List<String> statusCodeList);
    Optional<CustomerEntity> findByCimsCustomerCode(String cimsCustomerCode);
    List<CustomerEntity> findAllByCimsCustomerCodeIn(List<String> cimsCustomerCodes);
}
