package pipeshub.streams.test;

import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiConsumer;

import org.junit.Test;

import pipeshub.streams.PublishingBaseStream;
import pipeshub.streams.StreamProvider;
import pipeshub.streams.test.proxy.TestStreamProviders;
import pipeshub.streams.text.FileStreamType;
import pipeshub.streams.text.Publishers;

public class TestContinuosBaseStream{


	//@Test
	public void testContinuousStream(){
		StreamProvider<InputStream> provider = TestStreamProviders.fastPublishingInputStreamProvider();
		FileStreamType type = FileStreamType.LINE;
		BiConsumer<InputStream, BlockingQueue<String>> mapper = Publishers.inputStreamPublisher(type);
		boolean continuous = true;
		int bufferSize = 10;
		PublishingBaseStream<String, InputStream> baseStream = new PublishingBaseStream<>(provider, continuous, type, mapper, bufferSize);
		
		baseStream.stream().
				   map(line -> Integer.parseInt(line)).
				   filter(line -> line > 10).
				   forEach(line -> {System.out.println(line);return;});
		
	}

	@Test
	public void testNonContinuousStream(){
		StreamProvider<InputStream> provider = TestStreamProviders.fastPublishingInputStreamProvider();
		FileStreamType type = FileStreamType.LINE;
		BiConsumer<InputStream, BlockingQueue<String>> mapper = Publishers.inputStreamPublisher(type);
		boolean continuous = false;
		int bufferSize = 10;
		PublishingBaseStream<String, InputStream> baseStream = new PublishingBaseStream<>(provider, continuous, type, mapper, bufferSize);
		
		//baseStream.stream().
			//	   map(line -> Integer.parseInt(line)).
				//   filter(line -> line > 10).
				  // forEach(line -> {System.out.println(line);});
		baseStream.stream().forEach(line -> {System.out.println(line);});
	}

}
