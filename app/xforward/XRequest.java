package xforward;

import play.Play;
import play.mvc.Http.Request;

public class XRequest {

	private String host;
	private String remoteAddress;
	private boolean isSecure;

	public XRequest() {
		parseRequest();
	}

	private void parseRequest() {

		Request req = play.mvc.Http.Context.current().request();

		Boolean xForwardedSupport = new Boolean(Play.application().configuration().getString("XForwardedSupport"));

		remoteAddress = req.getHeader("x-forwarded-for");
		if (xForwardedSupport.booleanValue() && remoteAddress != null) {

			//TODO: Once Play2 has the remoteAddress, validate the following
	        //if (!Arrays.asList(Play.configuration.getProperty("XForwardedSupport", "127.0.0.1").split("[\\s,]+")).contains(remoteAddress)) {
	        //    throw new RuntimeException("This proxy request is not authorized: " + remoteAddress);
			//else

			isSecure = isRequestSecure(req);

			host = (String) Play.application().configuration().getString("XForwardedHost");
			if (host == null || host.trim().length() == 0) {

				host = req.getHeader("x-forwarded-host");
				if (host == null || host.trim().length() == 0) {
					host = req.host();
				}
			}
			
		} else {

			host = remoteAddress = req.host();
		}
	}

	private boolean isRequestSecure(Request req) {

		String xForwardedProtoHeader = req.getHeader("x-forwarded-proto");
		String xForwardedSslHeader = req.getHeader("x-forwarded-ssl");
		String frontEndHttpsHeader = req.getHeader("front-end-https");

		return ("https".equalsIgnoreCase(Play.application().configuration().getString("XForwardedProto"))
				|| "https".equalsIgnoreCase(xForwardedProtoHeader) || "on".equalsIgnoreCase(xForwardedSslHeader) || "on"
					.equalsIgnoreCase(frontEndHttpsHeader));
	}

	public String getHost() {
		return host;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public boolean isSecure() {
		return isSecure;
	}

}
