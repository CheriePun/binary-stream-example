In this repository I have written three tests to show the different ways of getting a Binary Stream from reading a column of type VARBINARY(MAX).

#### Database
The database in use is called "test". Running liquibase will give you the table used in the test

#### Test Results
Using the old jdbc driver 4.0 will pass the test getBlobGetBinaryStream() but not the other two.
Using the newest jdbc driver 6.5.2.jre8-preview will fail all three tests.