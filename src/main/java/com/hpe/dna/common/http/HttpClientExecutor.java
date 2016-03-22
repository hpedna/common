package com.hpe.dna.common.http;

import com.hpe.dna.common.AppRuntimeException;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Wrapper for Http Client
 * @author chun-yang.wang@hpe.com
 */
@Named
public class HttpClientExecutor {
    private static final Logger logger = getLogger(HttpClientExecutor.class);

    private HttpClient httpClient;

    @PostConstruct
    public void _init() {
        httpClient = createTrustAllHttpClient();
    }

    public HttpClientResponse get(String uri) {
        return get(uri, null);
    }

    public HttpClientResponse get(String uri, List<Header> headers) {
        HttpGet get = new HttpGet(uri);
        if (headers != null && headers.size() > 0) {
            headers.forEach(get::addHeader);
        }
        _mayAddProxy(get);
        try {
            return extract(httpClient.execute(get));
        } catch (IOException e) {
            logger.error("Failed to execute HTTP GET method - {}", uri, e);
            return null;
        } finally {
            get.releaseConnection();
        }
    }

    public HttpClientResponse postJson(String uri, String postData) {
        return postJson(uri, postData, null);
    }

    public HttpClientResponse postJson(String uri, String postData, List<Header> headers) {
        HttpPost post = new HttpPost(uri);
        _mayAddProxy(post);
        post.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
        if (headers != null && headers.size() > 0) {
            headers.forEach(post::addHeader);
        }
        try {
            StringEntity entity = new StringEntity(postData);
            post.setEntity(entity);
            return extract(httpClient.execute(post));
        } catch (IOException e) {
            logger.error("Failed to execute HTTP POST method - {}", uri, e);
            return null;
        } finally {
            post.releaseConnection();
        }
    }

    public HttpClientResponse put(String uri) {
        return put(uri, null);
    }

    public HttpClientResponse put(String uri, List<Header> headers) {
        HttpPut put = new HttpPut(uri);
        _mayAddProxy(put);
        if (headers != null && headers.size() > 0) {
            headers.forEach(put::addHeader);
        }
        try {
            return extract(httpClient.execute(put));
        } catch (IOException e) {
            logger.error("Failed to execute HTTP PUT method - {}", uri, e);
            return null;
        } finally {
            put.releaseConnection();
        }
    }

    private HttpClientResponse extract(HttpResponse response) throws IOException {
        String data = EntityUtils.toString(response.getEntity());
        int statusCode = response.getStatusLine().getStatusCode();
        return new HttpClientResponse(statusCode, data);
    }


    public boolean _mayAddProxy(HttpRequestBase request) {
        String host = request.getURI().getHost();
        if ("localhost".equals(host) || "127.0.0.1".equals(host)) {
            return false;
        }
        HttpHost proxy = HttpHost.create("http://web-proxy.atl.hp.com:8080");
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        request.setConfig(config);
        return true;
    }

    private HttpClient createTrustAllHttpClient() {
        HttpClientBuilder b = HttpClientBuilder.create();

        // setup a Trust Strategy that allows all certificates.
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            logger.error("Failed to create http client");
            throw new AppRuntimeException("Failed to create http client", e);
        }
        b.setSSLContext(sslContext);

        // don't check Hostnames, either.
        //      -- use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;

        // here's the special part:
        //      -- need to create an SSL Socket Factory, to use our weakened "trust strategy";
        //      -- and create a Registry, to register it.
        //
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();

        // now, we create connection-manager using our Registry.
        //      -- allows multi-threaded use
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        b.setConnectionManager(connMgr);

        // finally, build the HttpClient;
        //      -- done!
        return b.build();
    }

}
