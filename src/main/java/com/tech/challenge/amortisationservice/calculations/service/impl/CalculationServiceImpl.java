package com.tech.challenge.amortisationservice.calculations.service.impl;

import com.tech.challenge.amortisationservice.calculations.service.CalculationService;
import com.tech.challenge.amortisationservice.model.ScheduleDetailsDTO;
import com.tech.challenge.amortisationservice.repository.LoanDetailsRepository;
import com.tech.challenge.amortisationservice.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.tech.challenge.amortisationservice.model.LoanDetails;
import com.tech.challenge.amortisationservice.model.Schedule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculationServiceImpl implements CalculationService {

    private final ScheduleRepository scheduleRepository;
    private final LoanDetailsRepository loanDetailsRepository;

    @Override
    public void createPaymentSchedule(LoanDetails loanDetails) {

        Long assetCost = loanDetails.getAssetCost();
        Long depositAmount = loanDetails.getDepositAmount();
        double interestRate = loanDetails.getYearlyInterestRate() / 12;
        int monthlyPayments = loanDetails.getMonthlyPayments();
        Long balloonPayment = loanDetails.getBalloonPayment();

        double initialBalance = assetCost - depositAmount;
        double initialInterestPortion = calculateInterestPortionPayment(initialBalance, interestRate);
        double monthlyRepayment;

        if (balloonPayment == null) {
            monthlyRepayment = calculateMonthlyRepayment(assetCost, depositAmount, interestRate, monthlyPayments);
        } else {
            monthlyRepayment = calculateMonthlyRepaymentWithBalloonPayment(assetCost, depositAmount, interestRate, monthlyPayments, balloonPayment);
        }

        List<Schedule> scheduleList = new ArrayList<>();
        Schedule firstPeriodSchedule = new Schedule();
        firstPeriodSchedule.setPeriod(1);
        firstPeriodSchedule.setMonthlyRepayment(monthlyRepayment);
        firstPeriodSchedule.setPrincipal(monthlyRepayment - initialInterestPortion);
        firstPeriodSchedule.setInterest(initialInterestPortion);
        firstPeriodSchedule.setBalance(initialBalance - firstPeriodSchedule.getPrincipal());
        firstPeriodSchedule.setCaseId(loanDetails.getCaseId());

        scheduleList.add(firstPeriodSchedule);

        for (int i = 2; i <= monthlyPayments; i++) {
            Schedule schedule = new Schedule();
            schedule.setPeriod(i);
            schedule.setMonthlyRepayment(monthlyRepayment);

            double balance = scheduleList.get(i-2).getBalance();

            schedule.setInterest(calculateInterestPortionPayment(balance, interestRate));
            schedule.setPrincipal(calculatePrincipalPortion(monthlyRepayment, schedule.getInterest()));

            schedule.setBalance(Math.round((balance - schedule.getPrincipal()) * 100) / 100);
            schedule.setCaseId(loanDetails.getCaseId());
            scheduleList.add(schedule);

        }

        scheduleRepository.saveAll(scheduleList);
        loanDetailsRepository.saveAndFlush(loanDetails);

    }

    @Override
    public List<Schedule> retrieveFullPaymentSchedule(Long caseId) {

        return scheduleRepository.getScheduleByCaseId(caseId);

    }

    public List<ScheduleDetailsDTO> retrieveAllScheduleDetails() {

        List<ScheduleDetailsDTO> scheduleDetailsDTOList = new ArrayList<>();
        List<LoanDetails> loanDetailsList = loanDetailsRepository.findAllByIdIsNotNull();

        loanDetailsList.stream().forEach(loanDetails -> {
                    ScheduleDetailsDTO scheduleDetailsDTO = new ScheduleDetailsDTO();
                    scheduleDetailsDTO.setLoanDetails(loanDetails);

                    List<Schedule> scheduleList = scheduleRepository.getScheduleByCaseId(loanDetails.getCaseId());
                    double totalInterest = 0;
                    double totalPayment = 0;

            totalInterest += scheduleList.stream().mapToDouble(Schedule::getInterest).sum();
                    scheduleDetailsDTO.setTotalInterest(totalInterest);

            totalPayment += scheduleList.stream().mapToDouble(Schedule::getMonthlyRepayment).sum();
                    scheduleDetailsDTO.setTotalPayments(totalPayment);

                    scheduleDetailsDTO.setMonthlyRepayment(scheduleList.get(0).getMonthlyRepayment());

                    scheduleDetailsDTOList.add(scheduleDetailsDTO);
                }
        );

        return scheduleDetailsDTOList;
    }



    public double calculateMonthlyRepayment(Long assetCost, Long depositAmount, double interestRate, int monthlyPayments){
        log.trace("Entered calculateMonthlyRepayment({}, {}, {}, {}", assetCost, depositAmount, interestRate, monthlyPayments);

        Long amountToBeFinanced = assetCost - depositAmount;
        double monthlyRepayment = (amountToBeFinanced * interestRate) / (1 - Math.pow(1 + interestRate, - monthlyPayments));

        double roundedMonthlyRepayment = Math.round(monthlyRepayment * 100);

        return (roundedMonthlyRepayment / 100);

    }

    public double calculateMonthlyRepaymentWithBalloonPayment(Long assetCost, Long depositAmount, double interestRate, int monthlyPayments, Long balloonPayment) {
        log.trace("Entered calculateMonthlyRepaymentWithBalloonPayment({}, {}, {}, {}, {}", assetCost, depositAmount, interestRate, monthlyPayments, balloonPayment);

        Long P = assetCost - depositAmount;
        double monthlyRepayment = (P - (balloonPayment / Math.pow(1 + interestRate, monthlyPayments))) * (interestRate / (1 - (Math.pow(1 + interestRate, -monthlyPayments))));

        double roundedMonthlyRepayment = Math.round(monthlyRepayment * 100);

        return (roundedMonthlyRepayment / 100);
    }


    private double calculateInterestPortionPayment(double remainingBalance, double interestRate) {
        double roundedInterestRatePortion = Math.round((remainingBalance * interestRate) * 100);
        return roundedInterestRatePortion / 100;
    }


    private double  calculatePrincipalPortion(double totalPayment, double paymentTowardsInterest) {
        double roundedPrincipalPortion = Math.round((totalPayment - paymentTowardsInterest) * 100);
        return roundedPrincipalPortion / 100;
    }


}
