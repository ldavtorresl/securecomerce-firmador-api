package sv.mh.fe.business;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import sv.mh.fe.configuration.BucketsProperties;
import sv.mh.fe.constantes.Constantes;
import sv.mh.fe.filter.FirmarDocumentoFilter;
import sv.mh.fe.models.CertificadoMH;
import sv.mh.fe.security.Cryptographic;
import sv.mh.fe.utils.CloudStorageService;
import sv.mh.fe.utils.FileUtils;

@Service
public class CertificadoBusiness {
	
	@Autowired
	private Cryptographic cryptographic;
	
	@Autowired
	private FileUtils fileUtilis;

	@Autowired
	private CloudStorageService cloudStorageService;

	@Autowired
	private BucketsProperties buckets;

	private static Logger logger = LoggerFactory.getLogger(CertificadoBusiness.class);		
	
	public CertificadoMH recuperarCertifiado(FirmarDocumentoFilter filter) throws IOException, NoSuchAlgorithmException {		
		XmlMapper xmlMapper = new XmlMapper();
		JavaTimeModule module = new JavaTimeModule();
		xmlMapper.registerModule(module);

		CertificadoMH certificado = null;
		String crypto = cryptographic.encrypt(filter.getPasswordPri(), Cryptographic.SHA512);
		// El bucket viene de la configuración (gcp.buckets.certificados), no escrito
		// a mano: tras separar los buckets, los certificados dejaron de estar en el
		// histórico. La ruta dentro del bucket no cambió. Ver BucketsProperties.
		String bucketName = buckets.getCertificados();
		String objectName = (filter.getAmbiente().contentEquals("DESARROLLO") ? "certificados-NONPRD" : "certificados-PRD") + "/"+ filter.getNit()+".crt";
		// Se registra el bucket junto con la ruta: sin el bucket, un "no existe el
		// archivo" no dice si falta el objeto o si se está mirando el lugar viejo.
		logger.info("Leyendo certificado de gs://{}/{}", bucketName, objectName);
		byte[] contenidoCertificado = cloudStorageService.readFileAsBytes(bucketName, objectName);
//		Path path = Paths.get(Constantes.DIRECTORY_UPLOADS +"/"+filter.getAmbiente()+"/",filter.getNit()+".crt");
		String contenido = fileUtilis.LeerArchivoDesdeBytes(contenidoCertificado);
		certificado = xmlMapper.readValue(contenido, CertificadoMH.class);
		
		if(certificado.getPrivateKey().getClave().equals(crypto)){
			return certificado;			
		}
		logger.info("Password no valido: "+certificado.getNit());
		return null;
	}
}
