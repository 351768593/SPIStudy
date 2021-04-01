package firok.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

public class Main
{
	public static void main(String[] args)
	{
		ServiceLoader<IService> loader = ServiceLoader.load(IService.class);
		Iterator<IService> iter = loader.iterator();

		int count = 0;
		while(iter.hasNext())
		{
			IService service = iter.next();
			System.out.println("\nfound one: "+service.getClass()+"\n");
			service.run();

			count++;
		}
		System.out.println("\ncount: "+count);
	}
}
