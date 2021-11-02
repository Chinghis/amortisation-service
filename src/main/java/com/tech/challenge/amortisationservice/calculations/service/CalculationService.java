package com.tech.challenge.amortisationservice.calculations.service;

import com.tech.challenge.amortisationservice.model.LoanDetails;
import com.tech.challenge.amortisationservice.model.Schedule;
import com.tech.challenge.amortisationservice.model.ScheduleDetailsDTO;

import java.util.List;

public interface CalculationService {

    void createPaymentSchedule(LoanDetails loanDetails);

    List<Schedule> retrieveFullPaymentSchedule(Long caseId);

    List<ScheduleDetailsDTO> retrieveAllScheduleDetails();
}
