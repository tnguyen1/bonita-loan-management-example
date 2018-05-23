/*
 * Copyright (C) 2018 Bonitasoft S.A.
 * Bonitasoft is a trademark of Bonitasoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * or Bonitasoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 */
package com.company.rest.api.client

import groovy.json.JsonSlurper
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Baptiste Mesta.
 */
class RESTClient {
	String url
	private JsonSlurper slurper = new JsonSlurper()

	private static final Logger logger = LoggerFactory.getLogger(RESTClient.class)

	QueryResponse post(String path) {
		return execute(path, 'POST')
	}

	QueryResponse put(String path, String body) {
		return execute(path, 'PUT', body)
	}

	QueryResponse get(String path) {
		return execute(path, 'GET')
	}

	QueryResponse delete(String path) {
		return execute(path, 'DELETE')
	}

	private QueryResponse execute(String path, METHOD) {
		def uri = url + path
		logger.debug("calling method: $METHOD | $uri")
		HttpURLConnection request = ((HttpURLConnection) new URL(uri).openConnection()).with({
			requestMethod = METHOD
			return it
		})
		try {
			return new QueryResponse(status: request.responseCode, content: parseResponse(request))
		} catch (Exception e) {
			logger.error("API call: $METHOD to $uri failed with message:" + e.getMessage())
			return new QueryResponse(status: request.responseCode, content: e.getMessage())
		}
	}

	private QueryResponse execute(String path, METHOD, String body) {
		HttpURLConnection request = ((HttpURLConnection) new URL(url + path).openConnection()).with({
			requestMethod = METHOD
			setRequestProperty("Content-Type", "application/json; charset=UTF-8")
			doOutput = true
			doInput = true
			return it
		})
		def outputStream = request.outputStream
		try {
			outputStream.write(body.getBytes("UTF-8"))
		} finally {
			outputStream.close()
		}
		try {
			return new QueryResponse(status: request.responseCode, content: parseResponse(request))
		} catch (Exception e) {
			return new QueryResponse(status: request.responseCode, content: e.getMessage())
		}
	}

	private Object parseResponse(HttpURLConnection request) {
		if (request.contentLength == 0) {
			return [:]
		}
		slurper.parse(request.inputStream)
	}

	static String encode(String string) {
		URLEncoder.encode(string, "UTF-8").replace("+", "%20")
	}
}