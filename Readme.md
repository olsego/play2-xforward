#Proxy forwarding


This module brings back the missing Proxy forwarding settings from Play1.

## What is the problem with Play2?

As awkward as it sounds, Play2 is missing **Request.remoteAddress**!

In other words the server doesn't known who calls it. Knowing the client address is a typically requirement for Javascript API such as _recaptcha_ or _browserid_, where it is used for security and validation purposes.

A way around this limitation is to use a Proxy server in front of Play2. The proxy server then serves the remoteAddress as an HTTP Header variable called "X-Forwarded-For".

But two problems remain:

1. To be consistant, the API needs to return the client address no matter what network topology is being used (proxy/no proxy)
2. If you are behind a firewall request.host() returns your filewall address



This module tries to bring back harmony by reimplementing the logic part of Play1.

It's not perfect! -> the lack of remoteAddress doesn't help!


##Configuration

The following properties should be set in `conf/application.conf`

###XForwardedHost

Overrides the X-Forwarded-Host HTTP header value – the original host requested by the client, for use with proxy servers.

    Default: X-Forwarded-Host HTTP header value.

###XForwardedProto

Sets the proxy request to SSL, overriding the X-Forwarded-Proto and X-Forwarded-SSL HTTP header values – the protocol originally requested by the client. For example:

    XForwardedProto=https

###XForwardedSupport

A comma-separated list of IP addresses that are allowed X-Forwarded-For HTTP request header values, used to restrict local addresses when an X-Forwarded-For request header is set by a proxy server.

    Default: 127.0.0.1

Note that at this point, filtering is not complete. Play2's Request needs to be enhanced with remoteAddress


#Sample configuration


**conf/application.conf**

```
XForwardedSupport=127.0.0.1
XForwardedProto=https
XForwardedHost=www.myserver.com
```

# How to use

Instanciate XRequest(), the rest is straightforward.

This should really be part of Play2.