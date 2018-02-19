LTSE Appplication Demo
This software is written in Java language.

This source code can be run in any compute that installed Java 1.7

1. To run code:
- Just copy Runable file ltse.jar and data folder (inluding firms.txt, symbols.txt, trades.csv) to the same clean folder.
For example, you can see the folder demo_ltse with structure as below:
	demo_ltse/ltse.jar
	demo_ltse/data/symbols.txt
	demo_ltse/data/firms.txt
	demo_ltse/data/trades.csv

- Double click to ltse.jar.
- After execution, open folder data to confirm 4 output files:

	demo_ltse/data/rokerIDs_Accepted.csv
	demo_ltse/data/brokerIDs_Rejected.csv
	demo_ltse/data/orders_Accepted.csv
	demo_ltse/data/orders_Rejected.csv


2. To view code:
- The code can be viwed using Eclipse that installed Maven or any other IDE that supported Maven.
- 

3. Summary solution:
- Using library opencsv to read all data in trades.csv, and storage in orders list.
- Read all information symbols.txt, and storage in HashSet symbols.
- Read all information brokers.txt, and storage in HashSet brokers.

- Proccess data follow  4 bellow requirements, and output all to processedOrders list:
	+ Change status of all order to "Rejected". Each order that validated and qualified, we will change status to "Accepted".
	+ Only orders that have values for the fields of ‘broker’, ‘symbol’, ‘type’, ‘quantity’, ‘sequence id’, ‘side’, and ‘price’ should be accepted.
	Solution: Just check all properties of each order is not blank, or not equals zero (if it is number type).
	+ Only orders for symbols actually traded on the exchange should be accepted
	Solution: Just check property symbol of each order must be storaged in HashSet symbols.
	+ Within a single broker’s trades ids must be unique. If ids repeat for the same broker, only the first message with a given id should be accepted.
	Solution: Create a HashMap for all Broker. Key is broker, value is a HashSet of broker's ids (like this: HashMap<String, HashSet<Integer>> hmBrokers).
	We will by pass current order if the the sequence_id of current order is existing in HashSet of current Broker.
	Otherwise, update broker's id of this current order to HashSet of current broker and update current broker and  HashSet to HashMap. 

	+ Each broker may only submit three orders per minute: any additional orders in  should be rejected
	Solution: Create a HashMap for all brokers that within 1 minute (Key is broker, value is count of broker in one minute).
	Reset this HashMap if time change to next minute.
	If the current order has a broker, that has number of count > 3, we will by pass current order. 
	Otherwise, we will update status to "Accepted" and add it to processedOrders list.

And now, just select information we want to output from processedOrders, and write to 4 files.

- Output 2 files with lists of the broker and id of accepted and rejected orders. 
- Output 2 files with lists of accepted orders and rejected orders. 

4. Test pattern:
- I tested all pattern of 4 above requirments.
- I also tested and catch all exceptions cases.

	

