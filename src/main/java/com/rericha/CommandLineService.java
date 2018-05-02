package com.rericha;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CommandLineService implements PizzaOrderService {

	private List<PizzaOrder> pizzaOrders = new ArrayList<PizzaOrder>();
	public static SimpleDateFormat dtFormatter = new SimpleDateFormat( "MM/dd/yyyy hh:mm:ss a" );
	
	@Override
	public List<PizzaOrder> getPizzaOrders() {
		return pizzaOrders;
	}
	/**
	 * 
	 * @param srcFileStr
	 * @return this service for method chaining
	 */
	@Override
	public CommandLineService read( String srcFileStr )
	{
		pizzaOrders.clear();
		Path srcPath = Paths.get(srcFileStr).toAbsolutePath();
		
		try (BufferedReader br = Files.newBufferedReader(srcPath)) { //BufferedReader and BufferedWriter default to UTF-8 which is fine for now.
			
			//Build pizza-order collection from input file.
			//A CSV-Parser seems overkill for this simple file structure.
			String line = null;
			while ((line = br.readLine()) != null) {
				if ( line.trim().equals("")) //Skip blank lines.
					continue;
				String[] fields = line.split("\t+"); //Source file can have one or more tabs as a field delimiter.
				
				//Skip the header which is identified by the second field header, "time"
				if ( fields[1].trim().equals("time"))
					continue;
				
				//Parse the Epoch.
				Date time = null;
				try {
					time = new Date( Long.valueOf(fields[1]) );
				}
			//Catch and rethrow exceptions as RuntimeException's in order to provide a more
				//user-friendly error message. The developer can see the actual throwable with runtimeException.getCause.
				//This allows for implementation of an uncaught exception handler to provide a unified way to present
				//error messages to the user. This also facilitates the PizzaOrderService interface since the method signature 
				//couldn't accomodate multiple "throws declarations" depending on the implemenation.
				catch (NumberFormatException nfe) 
				{
					throw new RuntimeException( "Error parsing Epoch time for " + fields[0], nfe );
				}
				pizzaOrders.add(new PizzaOrder(fields[0], time));
			}
			
		//Opted for a static final comparator class member of PizzaOrder rather than implementing Comparable.
			//This way we don't have to worry about breaking the contract between compareTo and equals.
			Collections.sort(pizzaOrders, PizzaOrder.byItem ); 
		}
		catch (IOException e) {
			throw new RuntimeException( "Error reading input file " + srcFileStr + ": " + e.getMessage(), e );
		}
		
		return this;
	}
	/**
	 * Sorts the pizza orders by order item lexicographically.
	 * 
	 * @return this service for method chaining 
	 */
	@Override
	public CommandLineService sort() {

		//Opted for a static final comparator class member of PizzaOrder rather than implementing Comparable.
			//This way we don't have to worry about breaking the contract between compareTo and equals.
			Collections.sort(pizzaOrders, PizzaOrder.byItem ); 

		return this;
	}
	
	/**
	 * 
	 * @param destFileStr
	 * @return the pizza-order collection for further processing.
	 */
	@Override
	public CommandLineService output( String destFileStr )
	{
		Path destPath = Paths.get(destFileStr).toAbsolutePath();
		
		try (BufferedWriter writer = Files.newBufferedWriter(destPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING ))
		{
			writer.write("Order\t\ttime\r\n" );
			for ( PizzaOrder order : pizzaOrders )
				writer.write(order.getItem() + "\t\t" + dtFormatter.format(order.getTime()) + "\r\n" );
		}
		catch( IOException ioe ) {
			throw new RuntimeException( "Error writing to output file " + destFileStr + ": " + ioe.getMessage(), ioe );
		}
		
		return this;
	}
	
}
