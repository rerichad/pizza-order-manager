package com.rericha;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.rule.OutputCapture;

/**
 * This test class is a mixture of unit and integration tests. There are three main components to the 
 * program:
 * <ul>
 * <li>Reading input;</li>
 * <li>Sorting;</li>
 * <li>Outputing sorted data</li>
 * </ul>
 * <p>Each component is tested with a separate test. The integration/end-to-end test is done on the main spring boot class, 
 * PizzaOrderManager, by calling it's public run method. The Spring Framework is not loaded for these tests 
 * by using the @RunWith and @SpringBootTest annotations. This is due to a limitation in the framework in that 
 * the framework calls the @SpringBootApplication's run method without any arguments (see https://sdqali.in/blog/2015/12/10/integration-testing-challenges-for-non-web-spring-applications/ for an explanation). 
 * Therefore, no source/destination file paths can be passed and littering the production code with Test profile defaults doesn't seem justified.
 * </p>
 *     
 * @author daver
 *
 */
public class CommandLineServiceTest {
	
	private PizzaOrderManager manager = new PizzaOrderManager();
	private CommandLineService service = new CommandLineService();
	private String srcFileStr = "src/main/resources/pizza_orders.txt";
	private String destFileStr = "src/main/resources/pizza_orders_sorted.txt";
	@Rule
	public OutputCapture cap = new OutputCapture();
	
	@Test
	public void integrationTest()
	{
		manager.run(srcFileStr, destFileStr);
		File f = new File( destFileStr );
		assertTrue( destFileStr + " should exist.", f.exists() );
		int lines = 0;
		try ( BufferedReader br = Files.newBufferedReader(f.toPath()) )
		{
			while ( br.readLine() != null )
				lines++;
		}
		catch( IOException ioe )
		{
		}
		assertTrue( "There should be 10 lines.", lines == 10 );
		
		//This would ordinarily be in a separate test. However, we should avoid creating two success messages: one by this test and one by a separate 
		//one.
		assertTrue( cap.toString().contains(destFileStr + " created successfully."));
	}
	
	@Test
	public void testReading()
	{
		service.read(srcFileStr);
		assertTrue( "There should be 9 pizza order objects created.", service.getPizzaOrders().size() == 9 );
	}
	
	@Test
	public void whenReadingNonExistentFileThenIOException()
	{
		Throwable t = null;
		try
		{
			service.read(srcFileStr+".nonexistent");
		}
		catch( RuntimeException r )
		{
			t = r.getCause();
		}
		assertTrue( "Should throw an IOException", t instanceof IOException );
	}
	
	@Test
	public void testSorting()
	{
		service.read(srcFileStr).sort();
		List<PizzaOrder> orders = service.getPizzaOrders();
		assertTrue( 
				orders.get(0).getItem().equals( "Bread" ) && 
				orders.get(1).getItem().equals("Meat") &&
				orders.get(2).getItem().equals("Pizza") &&
				orders.get(3).getItem().equals("VegVeg") &&
				orders.get(4).getItem().equals("bread") &&
				orders.get(5).getItem().equals("bread") &&
				orders.get(6).getItem().equals("meatMeaT") &&
				orders.get(7).getItem().equals("p1zza") &&
				orders.get(8).getItem().equals("pizza") );
	}
	
	@Test
	public void testOutputing()
	{
		service.read(srcFileStr).sort().output(destFileStr );
		File f = new File( destFileStr );
		
		assertTrue( "The output file should exist.", f.exists() );
		
		int lines = 0;
		try ( BufferedReader br = Files.newBufferedReader(f.toPath()) )
		{
			while ( br.readLine() != null )
				lines++;
		}
		catch( IOException ioe )
		{
		}
		assertTrue( "There should be 10 lines.", lines == 10 );
	}
	
	@Test
	public void whenWritingToReadOnlyFileThenIOException()
	{
		String destFileStr = "src/main/resources/po_sorted_readonly.txt";
		File f = new File( destFileStr );
		try
		{
			f.createNewFile();
		}
		catch( IOException ex )
		{
			throw new RuntimeException( ex );
		}
		f.setReadOnly();

		Throwable t = null;
		try
		{
			service.read(srcFileStr).sort().output(destFileStr );
		}
		catch( RuntimeException e )
		{
			t = e.getCause();
		}
		assertTrue( "An IOException should be thrown.", t instanceof IOException );
	}
}
