package rg.self.documentmanagement;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class RsaKeyUtil {

	public static void main(String[] args) throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		KeyPair keyPair = generator.generateKeyPair();

		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();

		// Save private key in PKCS8 PEM format
		String privateKeyPem = "-----BEGIN PRIVATE KEY-----\n" + encodeToPem(privateKey.getEncoded())
				+ "-----END PRIVATE KEY-----\n";
		try (FileOutputStream fos = new FileOutputStream("private_key.pem")) {
			fos.write(privateKeyPem.getBytes());
		}

		// Save public key in X.509 PEM format
		String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" + encodeToPem(publicKey.getEncoded())
				+ "-----END PUBLIC KEY-----\n";
		try (FileOutputStream fos = new FileOutputStream("public_key.pem")) {
			fos.write(publicKeyPem.getBytes());
		}

		System.out.println("RSA 2048-bit key pair generated and saved as private_key.pem and public_key.pem");
	}

	private static String encodeToPem(byte[] keyBytes) {
		return Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(keyBytes) + "\n";
	}
}
