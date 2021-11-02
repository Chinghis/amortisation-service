package com.tech.challenge.amortisationservice.calculations.controller;

import com.tech.challenge.amortisationservice.calculations.controller.constant.EndPoint;
import com.tech.challenge.amortisationservice.calculations.service.CalculationService;
import com.tech.challenge.amortisationservice.model.Schedule;
import com.tech.challenge.amortisationservice.model.ScheduleDetailsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.tech.challenge.amortisationservice.model.LoanDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
public class AmortisationController {

    private final CalculationService calculationService;

    @PostMapping(value = EndPoint.CREATE_AMORTISATION_SCHEDULE)
    public ResponseEntity<Void> calculateAmortisationSchedule(@RequestBody LoanDetails loanDetails) {
        log.trace("Entered calculateAmortisationSchedule");

        calculationService.createPaymentSchedule(loanDetails);

        return ResponseEntity.ok().build();

    }

    @GetMapping(value = EndPoint.RETRIEVE_SPECIFIC_SCHEDULE)
    public ResponseEntity<List<Schedule>> retrieveSchedule(@PathVariable Long caseId) {
        log.trace("Entered retrieveSchedule('{}')", caseId);

        List<Schedule> scheduleList = calculationService.retrieveFullPaymentSchedule(caseId);

        return ResponseEntity.ok(scheduleList);
    }

    @GetMapping(value = EndPoint.RETRIEVE_ALL_SCHEDULE_DETAILS)
    public ResponseEntity<List<ScheduleDetailsDTO>> retrieveAllScheduleDetails()

    {
        log.trace("Entered retrieveAllScheduleDetails");

        List<ScheduleDetailsDTO> scheduleDetailsDTOList = calculationService.retrieveAllScheduleDetails();

        return ResponseEntity.ok(scheduleDetailsDTOList);
    }


}
