package com.leaderus.cache.disruptor_demo;

import com.lmax.disruptor.EventHandler;

public class LongEventHandler implements EventHandler<LongEvent> {

	public void onEvent(LongEvent longEvent, long sequence, boolean endOfBanch) throws Exception {
		System.out.println("Event : = " + longEvent);
	}

}
