package demo.docusign;

import java.io.IOException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;




@Controller
public class SignedController {

	@Value("${docusign.oauth.accesstoken}") 
	String accessToken;
	
	@GetMapping("/download")
	public ResponseEntity<byte[]> getFile(HttpSession session) throws ApiException, IOException {
		
		String basePath = "https://demo.docusign.net/restapi";
	    Long tokenExpirationSeconds = 8 * 60 * 60L;
		String accountId = "10403133";
	    final String HTTP_CONTENT_DISPOSITION_VALUE = "inline;filename=";
	    
	    
		 ApiClient apiClient = new ApiClient(basePath);
		 apiClient.setAccessToken(accessToken, tokenExpirationSeconds);
		 EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
		
		 String documentId = "1";

		 byte[] results = envelopesApi.getDocument(accountId,(String) session.getAttribute("envelopId"), documentId);
		 
		 String docName=(String) session.getAttribute("type");
		    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, HTTP_CONTENT_DISPOSITION_VALUE + docName)
		    		.contentLength(results.length)
		    		.contentType(MediaType.APPLICATION_PDF).body(results);
	}
	
}
