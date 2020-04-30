package demo.docusign;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.RecipientViewRequest;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;
import com.docusign.esign.model.ViewUrl;
import com.sun.jersey.core.util.Base64;

@Controller
public class DemoController {

	@PostMapping("/sign")
	public Object create(ModelMap model) throws IOException,ApiException {
		
		 String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IjY4MTg1ZmYxLTRlNTEtNGNlOS1hZjFjLTY4OTgxMjIwMzMxNyJ9.eyJUb2tlblR5cGUiOjUsIklzc3VlSW5zdGFudCI6MTU4ODEzOTcyMywiZXhwIjoxNTg4MTY4NTIzLCJVc2VySWQiOiJmNGIwYzY4MS1iMGM3LTQzZmMtYjZlNi1hNWIzMDdhYzYyNmMiLCJzaXRlaWQiOjEsInNjcCI6WyJzaWduYXR1cmUiLCJjbGljay5tYW5hZ2UiLCJvcmdhbml6YXRpb25fcmVhZCIsInJvb21fZm9ybXMiLCJncm91cF9yZWFkIiwicGVybWlzc2lvbl9yZWFkIiwidXNlcl9yZWFkIiwidXNlcl93cml0ZSIsImFjY291bnRfcmVhZCIsImRvbWFpbl9yZWFkIiwiaWRlbnRpdHlfcHJvdmlkZXJfcmVhZCIsImR0ci5yb29tcy5yZWFkIiwiZHRyLnJvb21zLndyaXRlIiwiZHRyLmRvY3VtZW50cy5yZWFkIiwiZHRyLmRvY3VtZW50cy53cml0ZSIsImR0ci5wcm9maWxlLnJlYWQiLCJkdHIucHJvZmlsZS53cml0ZSIsImR0ci5jb21wYW55LnJlYWQiLCJkdHIuY29tcGFueS53cml0ZSJdLCJhdWQiOiJmMGYyN2YwZS04NTdkLTRhNzEtYTRkYS0zMmNlY2FlM2E5NzgiLCJhenAiOiJmMGYyN2YwZS04NTdkLTRhNzEtYTRkYS0zMmNlY2FlM2E5NzgiLCJpc3MiOiJodHRwczovL2FjY291bnQtZC5kb2N1c2lnbi5jb20vIiwic3ViIjoiZjRiMGM2ODEtYjBjNy00M2ZjLWI2ZTYtYTViMzA3YWM2MjZjIiwiYXV0aF90aW1lIjoxNTg4MTM5NTA1LCJwd2lkIjoiNTIwZDExNDctZDYwMC00YWNmLWE1NGUtY2Q2MWUyYzYxZTdlIn0.lp5sNz3Q530SBdcW0D6y1PvtY0ynd8eWU7VwfUF6cY4YIBXUpWe_Fl1PwfKo6pX5TLxO82F2DEsM7X59HYlwT8JSUOiw-9kE4aQPJUPbZkfE6r7h7zOWyd9HpKhA9VKFsl6rQK1aWV28v_14ahxFvaW9fd4Q3bbRI8Tk3KRYHgk_7C-Ay2DwdvDEzVZNI2mPBu_WuvhIVP-L2AJ5dQoUQyvULzuch70Scyy6sr7kz15ffSeO14bweBpJtQ0KCRE1bEKfmReoAujXKUpTWrLeU3rbfG8kFD042dMpSo5xTmnT62BMl5zabmM8uqQccU_OTkpaSsxDecwKV4j0ETag1g"; 
	    Long tokenExpirationSeconds = 8 * 60 * 60L;

	    String accountId = "10403133";
        String signerName = "Sai Pavan";
        String signerEmail = "saipavan9999@yahoo.com";

	    String baseUrl = "http://34.67.110.78:8080";
	    String clientUserId = "123";
	    
	    
	    String authenticationMethod = "None"; 
	    
	    
	    String basePath = "https://demo.docusign.net/restapi";
	    
	    String docPdf = "Eviction-Notice-California.pdf";
	    
	    byte[] buffer = readFile(docPdf);
        String docBase64 = new String(Base64.encode(buffer));
        
        Document document = new Document();
        document.setDocumentBase64(docBase64);
        document.setName("Demo document");
        document.setFileExtension("pdf");
        document.setDocumentId("1");
        
        Signer signer = new Signer();
        signer.setEmail(signerEmail);
        signer.setName(signerName);
        signer.clientUserId(clientUserId);
        signer.recipientId("1");
        
        
        SignHere signHere = new SignHere();
        signHere.setDocumentId("1");
        signHere.setPageNumber("1");
        signHere.setRecipientId("1");
        signHere.setTabLabel("SignHereTab");
        signHere.anchorString("Signature");
        signHere.anchorYOffset("-15");
//        signHere.setXPosition("195");
//        signHere.setYPosition("147");
        
        
        Tabs signerTabs = new Tabs();
        signerTabs.setSignHereTabs(Arrays.asList(signHere));
        signer.setTabs(signerTabs);
        
        
        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject("Please sign this document");
        envelopeDefinition.setDocuments(Arrays.asList(document));
        
        
        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(signer));
        envelopeDefinition.setRecipients(recipients);
        envelopeDefinition.setStatus("sent");
        
        
        ApiClient apiClient = new ApiClient(basePath);
        apiClient.setAccessToken(accessToken, tokenExpirationSeconds);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        EnvelopeSummary results = envelopesApi.createEnvelope(accountId, envelopeDefinition);
        String envelopeId = results.getEnvelopeId();

        
        RecipientViewRequest viewRequest = new RecipientViewRequest();

        viewRequest.setReturnUrl(baseUrl + "/home");
        viewRequest.setAuthenticationMethod(authenticationMethod);
        viewRequest.setEmail(signerEmail);
        viewRequest.setUserName(signerName);
        viewRequest.setClientUserId(clientUserId);
        // call the CreateRecipientView API
        ViewUrl results1 = envelopesApi.createRecipientView(accountId, envelopeId, viewRequest);
        
        
        String redirectUrl = results1.getUrl();
        RedirectView redirect = new RedirectView(redirectUrl);
        redirect.setExposeModelAttributes(false);
        return redirect;
	}
	
	
    private byte[] readFile(String path) throws IOException {
        InputStream is = DemoController.class.getResourceAsStream("/" + path);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }
	
	
}
