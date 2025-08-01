package sv.mh.fe.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.springframework.stereotype.Service;

@Service
public class FileUtils {
	
	static public String MEDIA_TYPE_APPLICATION_KEY = "application/pkcs8";
	static public String MEDIA_TYPE_APPLICATION_CRT = "application/x-x509-ca-cert";

	public FileUtils() {
		super();
	}
	
	/**
	 * Metodo para crar una archivo generico
	 * @param ruta
	 * @param contendio
	 * @throws FileNotFoundException
	 */
	public void crearArchivo(String ruta, String contendio) throws FileNotFoundException {
		PrintWriter archivo = new PrintWriter(ruta);
		archivo.print(contendio);
		archivo.flush();
		archivo.close();
	}
	
	/**
	 * Metodo para leer un archivo
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public String LeerArchivo(Path path) throws IOException{		
		StringBuilder contendido = new StringBuilder();
		FileReader archivo = new FileReader(path.toString());	
		try (BufferedReader buffer = new BufferedReader(archivo)) {
			String linea = buffer.readLine();
			while(linea != null) {
				contendido.append(linea);
				linea = buffer.readLine();
			}
		}
		return contendido.toString();
	}


	public String LeerArchivoDesdeBytes(byte[] bytes) throws IOException {
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8))) {
			StringBuilder contenido = new StringBuilder();
			String linea;
			while ((linea = buffer.readLine()) != null) {
				contenido.append(linea);
			}
			return contenido.toString();
		}
	}
	
}