package com.daoninhthai.crm.repository;

import com.daoninhthai.crm.entity.Deal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {

    List<Deal> findByStage(Deal.DealStage stage);

    Page<Deal> findByStage(Deal.DealStage stage, Pageable pageable);

    List<Deal> findByCompanyId(Long companyId);

    List<Deal> findByContactId(Long contactId);

    @Query("SELECT d.stage, COUNT(d), COALESCE(SUM(d.value), 0) " +
           "FROM Deal d GROUP BY d.stage ORDER BY d.stage")
    List<Object[]> calculateTotalValueByStage();

    @Query("SELECT COALESCE(SUM(d.value), 0) FROM Deal d WHERE d.stage = 'CLOSED_WON'")
    BigDecimal getTotalRevenue();

    long countByStage(Deal.DealStage stage);
}
