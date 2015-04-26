package my.thereisnospoon.webm;

import java.util.*;

public class Test {

	private Object o = new Object();

	@org.junit.Test
	public void test() throws Exception {

		synchronized (o) {
			o.wait(2000);
		}
	}
}
