package pipeshub.streams;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import pipeshub.streams.text.FileStreamType;

public class PublishingBaseStream<R,V> implements Iterator<R>{

	private final int FILE_TYPE_BUFFER_SIZE = 10000;

	private BlockingQueue<R> queue = null;
	private ExecutorService producer;
	private int bufferSize;
	private boolean continuous;
	private StreamProvider<V> provider;
	private volatile boolean publishing = false;
	private volatile boolean alreadyPublished = false;
	private FileStreamType type;
	private BiConsumer<V, BlockingQueue<R>> publishingMapper;

	
	public PublishingBaseStream(StreamProvider<V> provider,
								boolean continuous,
								FileStreamType type,
								BiConsumer<V, BlockingQueue<R>> mapper,
								int bufferSize) {
		this.bufferSize = bufferSize;
		this.provider = provider;
		this.publishingMapper = mapper;
		this.continuous = continuous;
		this.type = type;
		bufferSize = type.equals(FileStreamType.FILE)?FILE_TYPE_BUFFER_SIZE:15;
		queue = new ArrayBlockingQueue<>(this.bufferSize);
		producer = Executors.newFixedThreadPool(1);
	
	}
	
	private Runnable publisher = new Runnable() {
		public void run() {
				publishing = true;
				V stream = provider.stream();
				publishingMapper.accept(stream, queue);
				publishing = false;
		}
	};

	private void waitForPublisher(){
		while(true){
			if(queue.size() < bufferSize && provider.available()){
				producer.submit(publisher);
				if(queue.isEmpty()){
					while(true){
						if(!queue.isEmpty()){
							break;
						}
					}
				}
				if(!queue.isEmpty()){
					break;
				}

			}
			
		}
		
	}
	
	@Override
	public boolean hasNext() {
		if(continuous){
			if(!publishing || queue.isEmpty()){
				waitForPublisher();
				return true;
			}
		}else{
			if(!alreadyPublished){
				waitForPublisher();
				alreadyPublished = true;
				return true;
			}else{
				if(!publishing && queue.isEmpty()){
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public R next() {
		try{
			return queue.take();
		}catch(InterruptedException e){
			throw new NoSuchElementException();
		}
	}

	public Stream<R> stream(){
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this, Spliterator.IMMUTABLE), false);
	}

}
