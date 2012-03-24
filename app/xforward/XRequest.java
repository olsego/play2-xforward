package xforward;

import java.util.HashMap;
import java.util.Map;

import play.Play;
import play.mvc.Http.Request;

public class XRequest {

	private String host;
	private String remoteAddress;
	private Boolean isSecure;
	private Map<String, String> parameters;

	public XRequest() {
		parseRequest();
	}

	private void parseRequest() {

		Request req = play.mvc.Http.Context.current().request();

		String xForwardedSupport = Play.application().configuration()
				.getString("XForwardedSupport");

		remoteAddress = req.getHeader("x-forwarded-for");
		if (xForwardedSupport != null && remoteAddress != null) {

			// TODO: Once Play2 has the remoteAddress, validate the following
			// if
			// (!Arrays.asList(Play.configuration.getProperty("XForwardedSupport",
			// "127.0.0.1").split("[\\s,]+")).contains(remoteAddress)) {
			// throw new
			// RuntimeException("This proxy request is not authorized: " +
			// remoteAddress);
			// else

			host = (String) Play.application().configuration()
					.getString("XForwardedHost");
			if (host == null || host.trim().length() == 0) {

				host = req.getHeader("x-forwarded-host");
				if (host == null || host.trim().length() == 0) {
					host = req.host();
				}
			}

		} else
			host = remoteAddress = req.host();
	}

	public String getHost() {
		return host;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public boolean isSecure() {

		if (isSecure == null) {

			Request req = play.mvc.Http.Context.current().request();

			String xForwardedProtoHeader = req.getHeader("x-forwarded-proto");
			String xForwardedSslHeader = req.getHeader("x-forwarded-ssl");
			String frontEndHttpsHeader = req.getHeader("front-end-https");

			if ("https".equalsIgnoreCase(Play.application().configuration()
					.getString("XForwardedProto"))
					|| "https".equalsIgnoreCase(xForwardedProtoHeader)
					|| "on".equalsIgnoreCase(xForwardedSslHeader)
					|| "on".equalsIgnoreCase(frontEndHttpsHeader)) {

				isSecure = Boolean.TRUE;
			} else
				isSecure = Boolean.FALSE;
		}

		return isSecure.booleanValue();
	}

	/**
	 * Reads the request parameters, there is no methods in Play2 as of now...
	 * 
	 * @param request
	 * @return a map with the request parameters
	 */
	public Map<String, String> getParameters() {

		if (parameters == null) {

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
			parameters = data;
		}
		return parameters;
	}

}
