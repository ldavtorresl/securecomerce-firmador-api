package sv.mh.fe.utils;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class CloudStorageService {

    @Autowired
    private Storage storage;


    public CloudStorageService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    // 🟡 SUBIR archivo desde ruta local
    public void uploadFile(String bucketName, String objectName, String filePath) throws IOException {
        byte[] content = Files.readAllBytes(Paths.get(filePath));
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, content);
        System.out.println("Archivo subido: " + objectName);
    }


    // 🔴 ELIMINAR archivo
    public boolean deleteFile(String bucketName, String objectName) {
        boolean deleted = storage.delete(bucketName, objectName);
        if (deleted) {
            System.out.println("Archivo eliminado: " + objectName);
        } else {
            System.out.println("No se pudo eliminar (no encontrado): " + objectName);
        }
        return deleted;
    }

    // 📖 LEER archivo de texto como String (HTML, JSON, etc.)
    public String readFileAsString(String bucketName, String objectName) throws IOException {
        Blob blob = storage.get(bucketName, objectName);
        if (blob == null || !blob.exists()) {
            throw new IllegalArgumentException("El archivo no existe: " + objectName);
        }

        try (InputStream inputStream = new ByteArrayInputStream(blob.getContent());
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }

    // 📥 LEER archivo como byte[] (para Jasper u otros)
    public byte[] readFileAsBytes(String bucketName, String objectName) {
        Blob blob = storage.get(bucketName, objectName);
        if (blob == null || !blob.exists()) {
            throw new IllegalArgumentException("El archivo no existe: " + objectName);
        }
        return blob.getContent();
    }

    // 📋 LISTAR archivos del bucket
    public List<String> listFiles(String bucketName) {
        List<String> fileNames = new ArrayList<>();
        Bucket bucket = storage.get(bucketName);

        if (bucket == null) {
            throw new IllegalArgumentException("El bucket no existe: " + bucketName);
        }

        for (Blob blob : bucket.list().iterateAll()) {
            fileNames.add(blob.getName());
        }

        return fileNames;
    }
}
