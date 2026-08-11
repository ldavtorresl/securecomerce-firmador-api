package sv.mh.fe.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Buckets de Cloud Storage.
 *
 * <p>
 * El nombre del bucket estaba escrito a mano acá dentro ("securecomerce"), de
 * modo que este servicio seguía leyendo del bucket histórico aunque el backend
 * ya hubiera movido los certificados al bucket separado. Si se migran los
 * objetos y se borra el origen, el firmador deja de encontrar el certificado y
 * NINGUNA empresa puede emitir.
 *
 * <p>
 * Se usan a propósito los MISMOS nombres de propiedad que el backend
 * (`gcp.buckets.*`, ver `BucketsProperties` en securecomerce-backend): los dos
 * servicios se configuran con la misma variable de entorno
 * `GCP_BUCKETS_CERTIFICADOS` y no pueden quedar apuntando a lugares distintos.
 *
 * <p>
 * El valor por defecto sigue siendo el bucket histórico, así que desplegar este
 * cambio sin definir nada NO altera el comportamiento actual.
 */
@Component
public class BucketsProperties {

	/**
	 * Certificados de firma electrónica. Las RUTAS dentro del bucket no cambiaron
	 * con la migración (`certificados-PRD/{nit}.crt` y su equivalente NONPRD): lo
	 * único que cambia es en qué bucket viven.
	 */
	@Value("${gcp.buckets.certificados:${gcp.buckets.principal:securecomerce}}")
	private String certificados;

	public String getCertificados() {
		return certificados;
	}
}
