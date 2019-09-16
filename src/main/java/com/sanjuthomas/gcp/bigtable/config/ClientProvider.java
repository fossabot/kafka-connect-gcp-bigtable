package com.sanjuthomas.gcp.bigtable.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;

/**
 *
 * @author Sanju Thomas
 *
 */
public class ClientProvider {

  private static final Logger logger = LoggerFactory.getLogger(ClientProvider.class);
  private WriterConfig writerConfig;

  public ClientProvider(final WriterConfig writerConfig) {
    logger.info("ClientProvider is created by thread id {}.",Thread.currentThread().getId());
    this.writerConfig = writerConfig;
  }

  /**
   * Create a BigtableDataClient using the given WriterConfig.
   * 
   * @return BigtableDataClient
   * @throws IOException
   */
  public BigtableDataClient client() throws IOException {
    logger.info("BigtableDataClient is created for thread id {}", Thread.currentThread().getId());
    final BigtableDataSettings settings =
        BigtableDataSettings.newBuilder().setProjectId(this.writerConfig.project())
            .setInstanceId(this.writerConfig.instance())
            .setCredentialsProvider(FixedCredentialsProvider.create(credential())).build();
    return BigtableDataClient.create(settings);
  }

  private ServiceAccountCredentials credential() throws FileNotFoundException, IOException {
    final File credentialFile = new File(writerConfig.keyFile());
    try (FileInputStream seviceAccountStream = new FileInputStream(credentialFile)) {
      return ServiceAccountCredentials.fromStream(seviceAccountStream);
    }
  }
}
