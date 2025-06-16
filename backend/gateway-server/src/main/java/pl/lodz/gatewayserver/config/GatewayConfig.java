package pl.lodz.gatewayserver.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class GatewayConfig {

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create()
                .keepAlive(true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 15000)
                .responseTimeout(Duration.ofSeconds(15))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(15, TimeUnit.SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(15, TimeUnit.SECONDS))
                );
    }
}
