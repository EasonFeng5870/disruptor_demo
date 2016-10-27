package a.b.c;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.leaderus.cache.disruptor_demo.LongEvent;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class DemoMain {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		ThreadFactory threadFactory = Executors.defaultThreadFactory();
		final int bufferSize = 1024;//用来指定ring buffer的大小，必须是2的倍数
		Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, threadFactory);
		
		disruptor.handleEventsWith(DemoMain::handleEvent);
		disruptor.start();
		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
		ByteBuffer bb = ByteBuffer.allocate(8);
		for( long l = 0; true; l++) {
			bb.putLong(0, l);
			ringBuffer.publishEvent(DemoMain::translate, bb);
		}
	}

	public static void handleEvent(LongEvent event, long sequence, boolean endOfBatch) {
		System.out.println(event);
	}
	
	public static void translate(LongEvent event, long sequence, ByteBuffer buffer) {
		event.setValue(buffer.getLong(0));
	}
	
}
