package com.accenture.bars.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.bars.entity.Billing;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Integer> {

    public List<Billing> findByBillingCycleAndStartDateAndEndDate(
            int billingCycle, LocalDate startDate, LocalDate endDate);

}
