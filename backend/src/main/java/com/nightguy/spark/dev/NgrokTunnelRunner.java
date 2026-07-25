package com.nightguy.spark.dev;

import jakarta.annotation.PreDestroy;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class NgrokTunnelRunner implements ApplicationRunner {

  private static final Logger log = LoggerFactory.getLogger(NgrokTunnelRunner.class);

  @Value("${NGROK_TUNNEL_URL}")
  private String ngrokTunnelURL;

  private Process process;

  @Override
  public void run(@NonNull ApplicationArguments args) throws Exception {
    ProcessBuilder pb = new ProcessBuilder("ngrok", "http", "--url=" + ngrokTunnelURL, "8080");
    pb.redirectErrorStream(true);
    process = pb.start();

    log.info("Public ngrok dev URL: {}", ngrokTunnelURL);
    log.info("For debugging see ngrok web inspector at: http://127.0.0.1:4040/inspect/http");
  }

  @PreDestroy
  public void shutdown() {
    if (process != null) {
      process.destroy();
    }
  }
}
