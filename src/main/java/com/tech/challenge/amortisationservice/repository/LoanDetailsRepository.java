package com.tech.challenge.amortisationservice.repository;

import com.tech.challenge.amortisationservice.model.LoanDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LoanDetailsRepository extends JpaRepository<LoanDetails, Long>, JpaSpecificationExecutor<LoanDetails>{

    List<LoanDetails> findAllByIdIsNotNull();

}
