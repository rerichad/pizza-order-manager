package com.rericha;

import java.util.List;

/**
 * This interface supports the intent of this application to allow three modes of input/output:
 * <ul>
 * <li>Read orders from a file and write the sorted orders to a file, both files being specified on the command-line;</li>
 * <li>Read orders from and write sorted orders to a database;</li>
 * <li>Upload an order file from the user and return the sorted file to the user via a web browser.</li>
 * </ul>
 * The parameters to the read and output methods are String URN's. A URN uniquely identifies a resource without specifying it's location.
 * In the case of command-line input or web browser upload, the URN will be a file name. For database access, the URN will be a unique identifier 
 * for the list of orders to be sorted.
 *   
 * @author daver
 *
 */
public interface PizzaOrderService {

	public List<PizzaOrder> getPizzaOrders();
	public PizzaOrderService read( String urn ); 
	public PizzaOrderService sort();
	public PizzaOrderService output( String urn );
}
