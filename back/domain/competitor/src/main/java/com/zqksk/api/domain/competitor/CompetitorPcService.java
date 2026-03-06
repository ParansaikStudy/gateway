package com.zqksk.api.domain.competitor;

import org.springframework.data.domain.Page;

import java.util.List;

public interface CompetitorPcService {

    default CompetitorPc save(NewCompetitorPc newCompetitorPc) {throw new UnsupportedOperationException("Not implemented");}
    default List<CompetitorPc> getCompetitorPcList(){throw new UnsupportedOperationException("Not implemented");}

    default List<CompetitorPc> getCompetitorPcListWithCondition(SearchCompetitorPcRequest searchCompetitorPcRequest){
        throw new UnsupportedOperationException("Not implemented");
    }

    default Page<CompetitorPc> getCompetitorPcListWithConditionAndPaging(SearchCompetitorPcRequestWithPage searchCompetitorPcRequestWithPage){
        throw new UnsupportedOperationException("Not implemented");
    }

}
