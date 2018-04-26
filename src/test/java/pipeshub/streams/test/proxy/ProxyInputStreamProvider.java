package pipeshub.streams.test.proxy;

import java.io.InputStream;

import pipeshub.streams.StreamProvider;

public class ProxyInputStreamProvider implements StreamProvider<InputStream>{

	private int delay;
	
	public ProxyInputStreamProvider(int delay) {
		this.delay = delay;
	}
	@Override
	public InputStream stream() {
		return this.getClass().getClassLoader().getResourceAsStream("test.properties");
	}

	@Override
	public boolean available() {
		try {
			Thread.sleep(delay);
			return true;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
