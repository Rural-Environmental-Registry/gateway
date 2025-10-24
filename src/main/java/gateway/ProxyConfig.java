package gateway;

import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.netty.transport.ProxyProvider;

@Configuration
public class ProxyConfig {

  @Bean
  public HttpClientCustomizer proxyCustomizer() {
    String proxyHost = System.getenv("PROXY_HOST");
    String proxyPortStr = System.getenv("PROXY_PORT");

    // Verifica se ambas as variáveis estão definidas
    if (proxyHost != null && !proxyHost.isBlank() && proxyPortStr != null && !proxyPortStr.isBlank()) {
      try {
        int proxyPort = Integer.parseInt(proxyPortStr);

        return httpClient -> httpClient.proxy(proxy -> proxy
            .type(ProxyProvider.Proxy.HTTP)
            .host(proxyHost)
            .port(proxyPort));
      } catch (NumberFormatException e) {
        System.err.println("Valor inválido para PROXY_PORT: " + proxyPortStr);
        // Retorna um customizer que não aplica proxy
        return httpClient -> httpClient;
      }
    }

    // Não aplica proxy
    return httpClient -> httpClient;
  }
}
