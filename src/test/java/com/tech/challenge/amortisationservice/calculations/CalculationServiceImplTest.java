package com.tech.challenge.amortisationservice.calculations;

import com.tech.challenge.amortisationservice.calculations.service.impl.CalculationServiceImpl;
import com.tech.challenge.amortisationservice.model.LoanDetails;
import com.tech.challenge.amortisationservice.model.Schedule;
import com.tech.challenge.amortisationservice.repository.LoanDetailsRepository;
import com.tech.challenge.amortisationservice.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CalculationServiceImplTest {

    private CalculationServiceImpl toTest;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @MockBean
    private LoanDetailsRepository loanDetailsRepository;

    @BeforeEach
    public void setUp(){
        toTest = new CalculationServiceImpl(scheduleRepository, loanDetailsRepository);
    }


    @Test
    public void testCreatePaymentScheduleSaves() {

        LoanDetails loanDetails = new LoanDetails();
        loanDetails.setAssetCost(20000l);
        loanDetails.setYearlyInterestRate(0.075);
        loanDetails.setDepositAmount(5000l);
        loanDetails.setMonthlyPayments(12);

        toTest.createPaymentSchedule(loanDetails);

        verify(scheduleRepository, times(1)).saveAll(any());
        verify(loanDetailsRepository, times(1)).saveAndFlush(any());

    }

    @Test
    public void testRetrieveAllDetails() {

        List<LoanDetails> loanDetailsList = new ArrayList<>();
        LoanDetails loanDetails = new LoanDetails();
        LoanDetails loanDetails1 = new LoanDetails();

        loanDetailsList.add(loanDetails);
        loanDetailsList.add(loanDetails1);

        Schedule schedule = new Schedule();
        schedule.setMonthlyRepayment(200);
        List<Schedule> scheduleList = new ArrayList<>();
        scheduleList.add(schedule);

        when(loanDetailsRepository.findAllByIdIsNotNull()).thenReturn(loanDetailsList);
        when(scheduleRepository.getScheduleByCaseId(any())).thenReturn(scheduleList);

        toTest.retrieveAllScheduleDetails();

        verify(scheduleRepository, times(2)).getScheduleByCaseId(any());
    }

    @Test
    public void testMonthlyRepaymentCalculatorWithoutBalloon() {

        Long assetCost = 20000l;
        Long depositAmount = 0l;
        double interestRate = 0.00625;
        int monthlyPayments = 60;

        double monthlyRepayment = toTest.calculateMonthlyRepayment(assetCost, depositAmount, interestRate, monthlyPayments);

        assertEquals(400.76, monthlyRepayment);

    }

    @Test
    public void testMonthlyPaymentCalculatorWithBalloon() {

        Long assetCost = 20000l;
        Long depositAmount = 0l;
        double interestRate = 0.00625;
        int monthlyPayments = 60;
        long balloonPayment = 10000l;

        double monthlyRepayment = toTest.calculateMonthlyRepaymentWithBalloonPayment(assetCost, depositAmount, interestRate, monthlyPayments, balloonPayment);

        assertEquals(262.88, monthlyRepayment);

    }
}
