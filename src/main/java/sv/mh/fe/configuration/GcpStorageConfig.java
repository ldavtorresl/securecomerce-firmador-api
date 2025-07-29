package sv.mh.fe.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sv.mh.fe.constantes.Constantes;

import java.io.FileInputStream;
import java.io.IOException;


@Configuration
public class GcpStorageConfig {

    @Bean
    public Storage storage() {
        return StorageOptions.getDefaultInstance().getService();
    }
}
