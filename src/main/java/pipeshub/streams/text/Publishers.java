package pipeshub.streams.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiConsumer;

public class Publishers {

	public static BiConsumer<InputStream, BlockingQueue<String>> inputStreamPublisher(FileStreamType type){
		return (stream, queue) -> {
			try(BufferedReader r = new BufferedReader(new InputStreamReader(stream))){
				String s = null;
				StringBuilder builder = null;
				boolean fileBuffer = type.equals(FileStreamType.FILE);
				if(fileBuffer){
					builder = new StringBuilder();
				}
				while((s = r.readLine()) != null){
					if(fileBuffer){
						builder.append(s);
					}else{
						queue.put(s);
					}
				}
				if(fileBuffer){
					queue.put(builder.toString());
				}
			}catch(IOException | InterruptedException e){
				e.printStackTrace();
			}

		};
								  
	}
	
	
	
	//public static BiConsumer<, U>
}
