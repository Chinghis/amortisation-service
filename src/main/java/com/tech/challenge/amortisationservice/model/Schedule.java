package com.tech.challenge.amortisationservice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Data
@Entity
public class Schedule {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ_SCHEDULE")
    private Long id;

    @Column
    private Long caseId;

    @Column
    private int period;

    @Column
    private double monthlyRepayment;

    @Column
    private double principal;

    @Column
    private double interest;

    @Column
    private double balance;
}
