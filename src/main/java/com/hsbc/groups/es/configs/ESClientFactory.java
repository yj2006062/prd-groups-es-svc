package com.hsbc.groups.es.configs;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ESClientFactory {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ESClientFactory.class);
	
	@Value("${cluster.name}")
	private String clusterName;
	@Value("${cluster.node}")
	private String clusterNode;
	@Value("${elasticsearch.host}")
	private String elasticServerHost;
	@Value("${elasticsearch.port}")
	private Integer elasticServerPort;
	@Value("${elasticsearch.protocal}")
	private String elasticServerProtocal;
	@Value("${searchguard.ssl.keystore_filapat}")
	private String elasticKeystorePath;
	@Value("${searchguard.ssl.truststore_filapat}")
	private String elasticTruststorePath;
	
	private String userName = "admin";
	
	private String password = "admin";
	
	private KeyStore truststore;
	private KeyStore keystore ;
	private String keypassword ="changgeit";

	private int maxRetryTimeoutMillis;

	private int maxConnTotal;

	private int maxConnPerRoute;
	
	@SuppressWarnings("deprecation")
	@Bean
	public RestHighLevelClient restHighLevelClient() {
		RestHighLevelClient restHighLevelClient = null;
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));
		
		try {
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(truststore, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// trust all
					return true;
				}
			}).build();
			restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(elasticServerHost, elasticServerPort,elasticServerProtocal))
					.setMaxRetryTimeoutMillis(maxRetryTimeoutMillis)
					.setHttpClientConfigCallback(httpClientBuilder->httpClientBuilder
							.setDefaultCredentialsProvider(credentialsProvider).setSSLContext(sslContext)
							.setSSLHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
							.setMaxConnTotal(maxConnTotal)
							.setMaxConnPerRoute(maxConnPerRoute)).setFailureListener(new RestClient.FailureListener() {
								@Override
								 public void onFailure(HttpHost host) {
										LOGGER.error("Could not initialize low level Rest client");
										System.exit(-1);
							        }
							}));
		} catch (Exception e) {
			LOGGER.error("Could not initialize high level Rest client with SSL error : "+e.getMessage());
		}
		return restHighLevelClient;
		
	}
	
	@Bean
	public TransportClient transportClient() throws UnknownHostException {
		
		Settings settings = Settings.builder().put("cluster.name",clusterName)
				.put("cluster.node",clusterNode)
				.put("searchguard.ssl.transport.enabled",true)
				.put("searchguard.ssl.transport.keystore_filepath","")
				.put("searchguard.ssl.transport.truststore_filepath","")
				.put("searchguard.ssl.transport.keystore_password","")
				.put("searchguard.ssl.transport.truststore_password","")
				.put("searchguard.ssl.transport.resolve_hostname",true)
				.put("client.transport.sniff", true)
				.put("path.home",".")
				.build();
		TransportClient transportClient = new PreBuiltTransportClient(settings,com.floragunn.searchguard.ssl.SearchGuardSSLPlugin.class);
		transportClient.filterWithHeader(Collections.singletonMap("Authorization", "Basic "+Base64.encodeBase64String((userName+password).getBytes())));
		transportClient.addTransportAddress(new TransportAddress(new InetSocketAddress(elasticServerHost, elasticServerPort)));
		return transportClient;
	}
	
	

}
