package com.zqksk.api.domain.competitor;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CompetitorPcFinder {

    private final CompetitorPcRepository competitorPcRepository;

    public List<CompetitorPc> getList(){return competitorPcRepository.findAll();}

    public List<CompetitorPc> getListWithCondition(String dateTypeName, LocalDate startDate, LocalDate endDate, List<Integer> pgTypeList,String searchType, String searchText){
        return competitorPcRepository.findAllWithCondition(dateTypeName, startDate, endDate ,pgTypeList,searchType, searchText);
    }
    public Page<CompetitorPc> getListWithConditionAndPaging(int page, int size, String dateTypeName, LocalDate startDate, LocalDate endDate, List<Integer> pgTypeList, String searchType, String searchText){
        return competitorPcRepository.findAllWithConditionAndPaging(page, size, dateTypeName, startDate, endDate, pgTypeList,searchType, searchText);
    }

    public List<CompetitorPc> getListByCompetitorInstallDateBetweenStartAndEnd(LocalDate startDate, LocalDate endDate){
        return competitorPcRepository.findAllByCompetitorInstallDateBetweenStartAndEnd(startDate,endDate);
    }

}
