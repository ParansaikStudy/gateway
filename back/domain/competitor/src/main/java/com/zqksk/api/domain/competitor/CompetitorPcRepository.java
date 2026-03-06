package com.zqksk.api.domain.competitor;

import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface CompetitorPcRepository {

    CompetitorPc save(final NewCompetitorPc newCompetitorPc);

    List<CompetitorPc> findAll();

    List<CompetitorPc> findAllWithCondition(String dateTypeName, LocalDate startDate, LocalDate endDate, List<Integer> pgTypeList, String searchType, String searchText);

    Page<CompetitorPc> findAllWithConditionAndPaging(int page, int size, String dateTypeName, LocalDate startDate, LocalDate endDate, List<Integer> pgTypeList, String searchType, String searchText);

    List<CompetitorPc> findAllByCompetitorInstallDateBetweenStartAndEnd(LocalDate startDate, LocalDate endDate);


}