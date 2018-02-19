package demo.ltse;

import com.opencsv.bean.CsvBindByPosition;

public class Order {
	@CsvBindByPosition(position = 0)
    public String timeStamp;
	@CsvBindByPosition(position = 1)
    public String broker;
	@CsvBindByPosition(position = 2)
    public int sequence_id;
	@CsvBindByPosition(position = 3)
    public String trade_type;
	@CsvBindByPosition(position = 4)
    public String symbol;
	@CsvBindByPosition(position = 5)
    public int quantity;
	@CsvBindByPosition(position = 6)
    public double price;
	@CsvBindByPosition(position = 7)
    public String side;
    public String status = "";
    
	public Order() {
		super();
	}
	public Order(String timeStamp, String broker, int sequence_id, String trade_type, String symbol, int quantity,
			double price, String side) {
		super();
		this.timeStamp = timeStamp;
		this.broker = broker;
		this.sequence_id = sequence_id;
		this.trade_type = trade_type;
		this.symbol = symbol;
		this.quantity = quantity;
		this.price = price;
		this.side = side;
	}
	
	public String toString(){
		return ("" + timeStamp + broker + sequence_id + trade_type + symbol + quantity + price + side + status);
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getBroker() {
		return broker;
	}
	public void setBroker(String broker) {
		this.broker = broker;
	}
	public int getSequence_id() {
		return sequence_id;
	}
	public void setSequence_id(int sequence_id) {
		this.sequence_id = sequence_id;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
    
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

}