package com.zqksk.api.domain.pc;

import org.springframework.data.domain.Page;

import java.util.List;

public interface PcService {
    default void save(NewPc newPc) {
        throw new UnsupportedOperationException("Not implemented");
    }
    default List<Pc> getPcList() {
        throw new UnsupportedOperationException("Not implemented");
    }
    default List<PcResponse> getPcListWithCondition(SearchPcRequest searchPcRequest) {
        throw new UnsupportedOperationException("Not implemented");
    }
    default Page<PcResponse> getPcListWithConditionAndPaging(SearchPcRequestWithPage searchPcRequest) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default List<PcResponse> getPcListWithMultipleConditions(MultiSearchPcRequest multiSearchPcRequest) {
        throw new UnsupportedOperationException("Not implemented");
    }
    default Page<PcResponse> getPcListWithMultipleConditionsAndPaging(MultiSearchPcRequestWithPage multiSearchPcRequestWithPage) {
        throw new UnsupportedOperationException("Not implemented");
    }
    default Pc getPc(Long id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default Pc getPcByMacAddress(String macAddress) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default List<Pc> getPmsUsageListBetweenStartAndDate(DateRangeRequest dateRangeRequest){throw new UnsupportedOperationException("Not implemented");}

    default List<PerformanceSpec> getAllPerformanceSpecBySoftwareId(int softwareId){throw new UnsupportedOperationException("Not implemented");}
}
