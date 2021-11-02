Running the project should automatically set up the amortisation.db file
Use this amortisation.db as a data source from file in SQLite 

To test "Create the amortisation schedule for a provided set of loan details (both with
and without a balloon payment)": Run application, using postman send the following POST request: 
"localhost:8102/calculate-schedule" 

Use the following JSON format when setting the request body (raw JSON):
{
"id" : null,
"caseId" : 1,
"assetCost" : 20000,
"depositAmount" : 0,
"yearlyInterestRate" : 0.075,
"monthlyPayments" : 12,
"balloonPayment" : null
}

Change "balloonPayment" to a number when wanting to check that.

and check the 'schedule' and 'loan_details' table to see if it has successfully created.

To test "List previously created schedules, returning:
   The details that were used to generate the schedule
   The monthly repayment amount that was calculated
   The total interest due
   The total payments due":
Using postman, send the following GET request:
"localhost:8102/retrieve-schedule/1" (1 being the case id, change as necessary to find specific case if you add more schedules)

To test "Retrieve the full details of an individual schedule, returning:
The contents of the list API for the schedule (Wasn't sure what this meant sorry!)
The amortisation schedule that was prepared":

Using postman, send the following get request:
"localhost:8102/retrieve-all-schedule-details"
This should bring back the main details of every schedule that has been set up



