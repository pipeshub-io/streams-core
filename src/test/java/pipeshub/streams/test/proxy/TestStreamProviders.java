package pipeshub.streams.test.proxy;

import java.io.InputStream;

import pipeshub.streams.StreamProvider;

public class TestStreamProviders {

	public static StreamProvider<InputStream> 	fastPublishingInputStreamProvider(){
		return new ProxyInputStreamProvider(20);
	}
	
	public static StreamProvider<InputStream> 	slowPublishingInputStreamProvider(){
		return new ProxyInputStreamProvider(20000);
	}
	
}
