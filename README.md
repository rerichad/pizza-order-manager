# pizza-order-manager
Once you clone this repo and change to your local repo directory, you can run the unit tests with:

mvn test

To run the program:

java -jar target\pizza-manager.jar {src file} {dest file}

where
src file = src\main\resources\pizza_orders.txt 
and
dest file = src\main\resources\pizza_orders_sorted.txt or any other output location you desire.

Note: don't use the output file-name src\main\resources\po_sorted_readonly.txt since this is used for 
one of the unit tests.

