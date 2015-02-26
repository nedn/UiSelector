package selector;

import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class Runner extends UiAutomatorTestCase {
	public static final int DEFAULT_PORT = 9008;
	SelectorHttpServer server;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		String portStringValue = getParams().getString("PORT");
		int port = DEFAULT_PORT;
		if (portStringValue != null) {
			port = Integer.parseInt(portStringValue);
		}
		server = new SelectorHttpServer(port);
		server.start();
		System.out.println("Server started");
	}

	@Override
	protected void tearDown() throws Exception {
		server.stop();
		System.out.println("Server stopped");
		server = null;
		super.tearDown();
	}

	public void testRun() throws InterruptedException {
		while (server.isAlive()) {
			synchronized (server.waitLock) {
				server.waitLock.wait();
			}
		}
	}
}
