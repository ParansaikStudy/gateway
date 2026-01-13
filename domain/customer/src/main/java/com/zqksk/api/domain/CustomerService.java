package com.zqksk.api.domain;

import java.util.List;

public interface CustomerService {
    List<CustomerLocation> getCustomerLocationList(RequestBounds requestBounds);
    List<CustomerLocationJob> getCustomerLocationJobList(RequestBounds requestBounds);
    ResponseCustomer getCustomerDetail(String cimsCustomerId);
    List<ResponseCustomer> getCustomerDetailList(List<String> cimsCustomerIds);
}
