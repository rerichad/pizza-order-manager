package com.rericha;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PizzaOrderManager implements CommandLineRunner {
	
//Autowired is not used since this class must be tested by JUnit without loading the Spring Testing Framework
	//See the CommandLineService class for an explanation. 
	private CommandLineService service = new CommandLineService(); 
	
	public final static void main( String[] args )
	{
		SpringApplication app = new SpringApplication(PizzaOrderManager.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}
	
	@Override
	public void run( String... args )
	{
		if ( args.length < 2 )
		{
			System.out.println( "Pizza Order Manager: sorts the given input file lexicographically and "
					+ "then outputs it as the given destination file.\n" +
					"Usage: java -jar pos.jar src-file dest-file" );
			System.exit(1);
		}
		
		service.read(args[0]).sort().output(args[1]);
		
		System.out.println(args[1] + " created successfully.");
	}
	
}
