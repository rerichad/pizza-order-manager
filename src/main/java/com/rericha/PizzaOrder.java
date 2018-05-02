package com.rericha;

import java.util.Comparator;
import java.util.Date;

public class PizzaOrder {
	
	public static final Comparator<PizzaOrder> byTime = new Comparator<PizzaOrder>(){

		/**
		 * If either pizzaOrder.time is null, sort it to the top.
		 */
		@Override
		public int compare( PizzaOrder o1, PizzaOrder o2 ) {
			if ( o1.getTime() == null || o2.getTime() == null )
				return 1;
			return o1.getTime().compareTo(o2.getTime());
		}
		
	};
	
	public static final Comparator<PizzaOrder> byItem = new Comparator<PizzaOrder>(){
		
		@Override
		public int compare( PizzaOrder o1, PizzaOrder o2 ) {
			return o1.getItem().compareTo(o2.getItem());
		}
		
	};
	
	private String item;
	private Date time;
	
	public PizzaOrder() {
		super();
	}

	public PizzaOrder( String item, Date time ) {
		super();
		this.item = item;
		this.time = time;
	}
	
	public String getItem() {
		return item;
	}
	
	public void setItem( String item ) {
		this.item = item;
	}
	
	public Date getTime() {
		return time;
	}
	
	public void setTime( Date time ) {
		this.time = time;
	}
	
	public int compareTo( PizzaOrder po )
	{
		return time.compareTo(po.getTime());
	}

}
