package demo.ltse;

import java.io.IOException;

import java.util.HashSet;
import java.util.List;


/**
 * LTSE Demo App
 *
 */
public class App 
{
    public static void main(String[] args) throws IOException {
        String csvFile = "data/trades.csv";
        String firmsFile = "data/firms.txt";
        String symbolsFile = "data/symbols.txt";
        String brokerIdsAcceptedFile = "data/brokerIDs_Accepted.csv";
        String brokerIdsRejectedFile = "data/brokerIDs_Rejected.csv";
        String ordersAcceptedFile = "data/orders_Accepted.csv";
        String ordersRejectedFile = "data/orders_Rejected.csv";
        
        
        Helper helper = new Helper();
        
        HashSet<String> symbols = helper.readSymbols(symbolsFile);
        HashSet<String> brokers = helper.readBrokers(firmsFile);
        List<Order> orders = helper.readCSV(csvFile);
        List<Order> processedOrders = helper.processOrders(orders, symbols, brokers);
        
        // Write all Broker and ID, which have status Accepted
        helper.writeBrokerIds(processedOrders, brokerIdsAcceptedFile, "Accepted");
        
        // Write all Broker and ID, which have status Rejected
        helper.writeBrokerIds(processedOrders, brokerIdsRejectedFile, "Rejected");
        
        // Write all orders, which have status Accepted
        helper.writeOrders(processedOrders, ordersAcceptedFile, "Accepted");
        
        // Write all orders, which have status Rejected
        helper.writeOrders(processedOrders, ordersRejectedFile, "Rejected");
        
        System.out.println("Processing completed. Please check output files in data folder.");
        
    }
}
