If I had more time to do this I would:

1.Create another service that calls the amortisation API so Postman wouldn't be needed
2.Change the calculations to BigDecimal to increase accuracy (Had an issue with rounding as you'll see)
3.Refactor the CalculationServiceImpl to reduce the size of the createPaymentSchedule method
4.Use more appropriate variable names
5. Fix the ID sequencers on the loan_details + schedule tables to stop the ID's going ridiculously high
6. Input validation on schedule creation request
