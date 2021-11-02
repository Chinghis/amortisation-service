package com.tech.challenge.amortisationservice.model;

import lombok.Data;;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Data
@Entity
public class LoanDetails {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ_LOAN_DETAILS")
    Long id;
    Long caseId;
    Long assetCost;
    Long depositAmount;
    double yearlyInterestRate;
    int monthlyPayments;
    Long balloonPayment;
}
