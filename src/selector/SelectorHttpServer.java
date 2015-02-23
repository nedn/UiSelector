package selector;
import fi.iki.elonen.NanoHTTPD;

public class SelectorHttpServer extends NanoHTTPD {

	public SelectorHttpServer(int port) {
		super(port);
	}

}
