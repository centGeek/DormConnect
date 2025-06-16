package pl.lodz.dormitoryservice.nfc.contig;

import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        HttpClient httpClient = HttpClients.custom()
                .setConnectionManager(createConnectionManager())
                .setKeepAliveStrategy(createKeepAliveStrategy())
                .build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        return new RestTemplate(factory);
    }

    private HttpClientConnectionManager createConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(20);
        connectionManager.setMaxTotal(200);
        return connectionManager;
    }

    private ConnectionKeepAliveStrategy createKeepAliveStrategy() {
        return (response, context) -> {
            // Set a custom keep-alive duration, e.g., 30 seconds
            return TimeValue.ofSeconds(15);
        };
    }
}