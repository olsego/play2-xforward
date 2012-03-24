package xforward;

import java.util.HashMap;
import java.util.Map;

import play.Play;
import play.mvc.Http.Request;

final public class SXRequest {

	private SXRequest() {
	}

	static public String getRemoteAddress() {

		Request req = play.mvc.Http.Context.current().request();

		String xForwardedSupport = Play.application().configuration()
				.getString("XForwardedSupport");

		String remoteAddress = req.getHeader("x-forwarded-for");
		if (xForwardedSupport != null && remoteAddress != null) {

			return remoteAddress;
		} else
			return req.host();
	}

	static public String getHost() {

		Request req = play.mvc.Http.Context.current().request();

		String xForwardedSupport = Play.application().configuration()
				.getString("XForwardedSupport");

		String remoteAddress = req.getHeader("x-forwarded-for");
		if (xForwardedSupport != null && remoteAddress != null) {

			String host = (String) Play.application().configuration()
					.getString("XForwardedHost");
			if (host == null || host.trim().length() == 0) {

				host = req.getHeader("x-forwarded-host");
				if (host == null || host.trim().length() == 0) {
					host = req.host();
				}
			}

			return host;
		} else
			return req.host();

	}

	static public boolean isSecure() {

		Request req = play.mvc.Http.Context.current().request();

		String xForwardedProtoHeader = req.getHeader("x-forwarded-proto");
		String xForwardedSslHeader = req.getHeader("x-forwarded-ssl");
		String frontEndHttpsHeader = req.getHeader("front-end-https");

		return ("https".equalsIgnoreCase(Play.application().configuration()
				.getString("XForwardedProto"))
				|| "https".equalsIgnoreCase(xForwardedProtoHeader)
				|| "on".equalsIgnoreCase(xForwardedSslHeader) || "on"
					.equalsIgnoreCase(frontEndHttpsHeader));
	}

	/**
	 * Reads the request parameters, there is no methods in Play2 as of now...
	 * 
	 * @param request
	 * @return a map with the request parameters
	 */
	static public Map<String, String> getParameters() {

		Request request = play.mvc.Http.Context.current().request();

		Map<String, String[]> urlFormEncoded = new HashMap<String, String[]>();
		if (request.body().asFormUrlEncoded() != null) {
			urlFormEncoded = request.body().asFormUrlEncoded();
		}

		Map<String, String[]> queryString = request.queryString();

		Map<String, String> data = new HashMap<String, String>();

		for (String key : urlFormEncoded.keySet()) {
			String[] value = urlFormEncoded.get(key);
			if (value.length > 0) {
				data.put(key, value[0]);
			}
		}

		for (String key : queryString.keySet()) {
			String[] value = queryString.get(key);
			if (value.length > 0) {
				data.put(key, value[0]);
			}
		}

		return data;
	}

}
