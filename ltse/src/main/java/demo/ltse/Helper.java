package demo.ltse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public class Helper {
	
	List<Order> readCSV(String csvFile){
	    // Read information from CSV file
		List<Order> orders = null;
	    try (Reader reader = new FileReader(csvFile);) {
	        CsvToBean<Order> csvToBean = new CsvToBeanBuilder<Order>(reader)
	                .withType(Order.class)
	                .withSkipLines(1) // skip header
	                .withIgnoreLeadingWhiteSpace(true)
	                .build();

	        orders = csvToBean.parse();
	        
	        reader.close();
	    }
	    catch (Exception e)
	    {
	      System.err.format("Exception occurred when trying to read '%s'.", csvFile);
	      e.printStackTrace();
	    }
	    
	    return orders;
		
	}

	HashSet <String> readBrokers(String firmsFile){
	    // Read from firmsFile
	    HashSet <String> brokers = new HashSet<String>();
	    try
	    {
	      BufferedReader reader = new BufferedReader(new FileReader(firmsFile));

	      String line;
	      while ((line = reader.readLine()) != null)
	      {
	    	  if(!brokers.contains(line))
	    	  brokers.add(line);
	      }
	      reader.close();
	      
	    }
	    catch (Exception e)
	    {
	      System.err.format("Exception occurred when trying to read '%s'.", firmsFile);
	      e.printStackTrace();
	    }
	    return brokers;
		
	}
    
	HashSet<String>  readSymbols(String symbolsFile){
	    // Read from symbolsFile
	    HashSet<String> symbols = new HashSet<String>();
	    
	    try
	    {
	      BufferedReader reader = new BufferedReader(new FileReader(symbolsFile));

	      String line;
	      while ((line = reader.readLine()) != null)
	      {
	    	  if(!symbols.contains(line))
	    		  symbols.add(line);
	      }
	      reader.close();
	    }
	    
	    catch (Exception e)
	    {
	      System.err.format("Exception occurred when trying to read '%s'.", symbolsFile);
	      e.printStackTrace();
	    }
		
	    return symbols;
	}
    
	List<Order> processOrders(List<Order> orders, HashSet<String> symbols, HashSet<String> brokers){
		List<Order> processedOrders = new ArrayList<Order>();
		HashMap<String, Integer> hmBrokersCount = new HashMap<String, Integer>(); // Key is broker, value is count of broker in one minute
		HashMap<String, HashSet<Integer>> hmBrokers = new HashMap<String, HashSet<Integer>>(); // Key is broker, value is broker's id
		
		String currentTime = orders.get(0).getTimeStamp().substring(0, 15);
		
		for (Order order : orders) {
			order.setStatus("Rejected");
			
			// Only orders that have values for the fields of ‘broker’, ‘symbol’, ‘type’, 
			// ‘quantity’, ‘sequence id’, ‘side’, and ‘price’ should be accepted.
            if(!(order.getTimeStamp().trim()).equals("")
            		&& !(order.getBroker().trim()).equals("")
            		&& order.getSequence_id() != 0
            		&& !(order.getTrade_type().trim()).equals("")
            		&& !(order.getSymbol().trim()).equals("")
            		&& order.getQuantity() != 0
            		&& order.getPrice() != 0
            		&& !(order.getSide().trim()).equals("")
            		)
            {
            	// Only orders for symbols actually traded on the exchange should be accepted
            	if(symbols.contains(order.symbol))
            	{
            		// Within a single broker’s trades ids must be unique. If ids repeat for the same broker, 
            		// only the first message with a given id should be accepted.
            		if(hmBrokers.containsKey(order.getBroker())){
        				if((hmBrokers.get(order.getBroker())).contains(order.getSequence_id()))
        					continue; // By pass if the the sequence_id of current order is existing in HashSet of current Broker.
        				else
        				{
            				HashSet<Integer> ids =  hmBrokers.get(order.getBroker());
            				ids.add(order.getSequence_id());			// Add sequence_id to HashSet of current Broker.
            				hmBrokers.replace(order.getBroker(), hmBrokers.get(order.getBroker()), ids );		// Update current Broker
        				}
        			}
        			else
        			{
        				HashSet<Integer> ids = new HashSet<Integer>();
        				ids.add(order.getSequence_id());			// Add sequence_id to HashSet of current Broker.
        				hmBrokers.put(order.getBroker(), ids );		// Update current Broker
        			}
        			
            		// Check within 1 minutes
            		if((order.getTimeStamp().substring(0, 15)).equals(currentTime)){
            			if(hmBrokersCount.containsKey(order.getBroker())){
            				//Each broker may only submit three orders per minute: any additional orders in  should be rejected
            				if(hmBrokersCount.get(order.getBroker()).intValue() < 3){
            					hmBrokersCount.put(order.getBroker(), (hmBrokersCount.get(order.getBroker())).intValue() + 1);
            					order.setStatus("Accepted");
            					
            				}
            			}
            			else{	// If broker is not existing in hashmap, just add it to hashmap and asign status is Accepted
            				hmBrokersCount.put(order.getBroker(), 1);
            				order.setStatus("Accepted");
            				
            			}
            			
            		}
    				else{
    					hmBrokersCount.clear();
    					currentTime = order.getTimeStamp().substring(0, 15);
            				hmBrokersCount.put(order.getBroker(), 1);
            				order.setStatus("Accepted");
    					
    				}
            		
            	}
            	
            }
            processedOrders.add(order);
            
        }
		return processedOrders;
	}
	
	
	void writeBrokerIds(List<Order> processedOrders, String brokerIdsdFile, String status){
        try (Writer writer = new FileWriter(brokerIdsdFile);
                CSVWriter csvWriter = new CSVWriter(writer, 
                        CSVWriter.DEFAULT_SEPARATOR, 
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
                        CSVWriter.DEFAULT_LINE_END);) {
            String[] headerRecord = { "broker", "sequence id"};
            csvWriter.writeNext(headerRecord);
            for (Order order : processedOrders) {
            	if(order.getStatus().equals(status))
                csvWriter.writeNext(new String[] {order.getBroker(), Integer.toString(order.getSequence_id())});
            }
        }
	    catch (Exception e)
	    {
	      System.err.format("Exception occurred when trying to write '%s'.", brokerIdsdFile);
	      e.printStackTrace();
	    }
	}
	
	void writeOrders(List<Order> processedOrders, String ordersFile, String status){
        try (Writer writer = new FileWriter(ordersFile);
                CSVWriter csvWriter = new CSVWriter(writer, 
                        CSVWriter.DEFAULT_SEPARATOR, 
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
                        CSVWriter.DEFAULT_LINE_END);) {
            String[] headerRecord = { "Time stamp",	"broker", "sequence id", "type", "Symbol", "Quantity", "Price", "Side"};
            csvWriter.writeNext(headerRecord);
            for (Order order : processedOrders) {
            	if(order.getStatus().equals(status))
                csvWriter.writeNext(new String[] {order.getTimeStamp(), order.getBroker(), 
                		Integer.toString(order.getSequence_id()),order.getTrade_type(), order.getSymbol(), 
                		Integer.toString(order.getQuantity()), Double.toString(order.getPrice()), order.getSide()});
            }
        }
	    catch (Exception e)
	    {
	      System.err.format("Exception occurred when trying to write '%s'.", ordersFile);
	      e.printStackTrace();
	    }
	}
	
	
}
