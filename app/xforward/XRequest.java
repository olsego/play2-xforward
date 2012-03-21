package xforward;

import play.Play;
import play.mvc.Http.Request;

public class XRequest {

	private String host;
	private String remoteAddress;
	private boolean isSecure;

	public void parseXForwarded() {

		Request req = play.mvc.Http.Context.current().request();

		String xForwardedSupport = Play.application().configuration().getString("XForwardedSupport");

		if (xForwardedSupport != null && req.getHeader("x-forwarded-for") != null) {

			isSecure = isRequestSecure(req);

			host = (String) Play.application().configuration().getString("XForwardedHost");
			if (host == null || host.trim().length() == 0) {

				host = req.getHeader("x-forwarded-host");
				if (host == null || host.trim().length() == 0) {
					host = req.host();
				}
			}

			remoteAddress = req.getHeader("x-forwarded-for");
		}
	}

	public boolean isRequestSecure(Request req) {

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
