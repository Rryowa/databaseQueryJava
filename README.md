# databaseQueryJava
Database query language (Insert, Select, Update, Delete) written in Java! (All commands are case insensitive '^_^)

~You can:

  - Insert values (with "Insert values" token).
  - Update values (with "Update values" token and "where" token), 
    that specifies which rows to update, in relation to the query after the "where" token.
  - Select (with "Insert values" token) rows that match your query after the "where" token.
  - Delete (with "Delete values" token) rows that match your query after the "where" token.

Main.java - main unit.

JavaSchoolStarter.java - main class that implements 4 methods: insert(), update(), select(), delete()

ParsedLogic.java - parseLogic() Splits an incoming query like: "age>25 and age<=40 OR age>30 and age<80 and age>25"
    into lists of strings, using an OR operator. Thus we get: {"age>25 and age<=40", "age>30 and age<80 and age>25"}.
    It is made by analogy with merge sort.

Comparative.java - implements generation of lists of indexes that matches given request. index - number of row
    from database (starts from 0). In order to then select() method of the JavaSchoolStarter class
    performed a logical union of an array previously separated by an OR operator.
