package com.tech.challenge.amortisationservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScheduleDetailsDTO {

    private LoanDetails loanDetails;
    private double monthlyRepayment;
    private double totalInterest;
    private double totalPayments;

}
