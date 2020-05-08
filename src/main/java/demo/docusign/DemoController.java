package demo.docusign;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.docusign.esign.model.TemplateRole;
import com.docusign.esign.model.ViewUrl;
import com.sun.jersey.core.util.Base64;

import demo.data.UserRepository;
import demo.model.User;
import demo.security.UserRepositoryUserDetailsService;

@Controller
public class DemoController {

	

	@Autowired
	  private static UserRepository userRepo;
	
	public DemoController(UserRepository userRepo) {
		DemoController.userRepo = userRepo;
		
	}
	  

	private static Signer createSigner(Principal principal) {
		
	  User user = userRepo.findByUsername(principal.getName());
	  Signer signer = new Signer();
	  signer.setEmail(user.getEmail());
	  signer.setName(user.getFirstName()+" "+user.getLastName());
	  signer.setRecipientId("1");
	  signer.setClientUserId(user.getId().toString());
	  return signer;
	}
	
	private Document getDoc(String formType) throws IOException {
	
		String docPdf = "";
		String name = "";
		  if(formType.equals("overdue")) {
			  docPdf = "Eviction-Notice-California.pdf";
			  name = "Rent Overdue";
		   }else if(formType.equals("lease_violation")) {
			   docPdf = "LeaseViolationspdf.pdf";
			   name = "Lease Violation";
		    }else {
		    	  docPdf = "Eviction-Notice-California.pdf";
		    	  name = "Other";
		    }
		  
		  System.out.println(formType);
		  System.out.println(docPdf);
		  
		    byte[] buffer = readFile(docPdf);
	        String docBase64 = new String(Base64.encode(buffer));
	        
	        Document document = new Document();
	        document.setDocumentBase64(docBase64);
	        document.setName(name+" document");
	        document.setFileExtension("pdf");
	        document.setDocumentId("1");
	        
	        return document;
	        
	}
	
	
	@PostMapping("/sign")
	public Object create(HttpServletRequest request,Principal principal) throws IOException,ApiException {
		
		String formType = request.getParameter("inlineRadioOptions");
		
		String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IjY4MTg1ZmYxLTRlNTEtNGNlOS1hZjFjLTY4OTgxMjIwMzMxNyJ9.eyJUb2tlblR5cGUiOjUsIklzc3VlSW5zdGFudCI6MTU4ODkxMjgyMSwiZXhwIjoxNTg4OTQxNjIxLCJVc2VySWQiOiJmNGIwYzY4MS1iMGM3LTQzZmMtYjZlNi1hNWIzMDdhYzYyNmMiLCJzaXRlaWQiOjEsInNjcCI6WyJzaWduYXR1cmUiLCJjbGljay5tYW5hZ2UiLCJvcmdhbml6YXRpb25fcmVhZCIsInJvb21fZm9ybXMiLCJncm91cF9yZWFkIiwicGVybWlzc2lvbl9yZWFkIiwidXNlcl9yZWFkIiwidXNlcl93cml0ZSIsImFjY291bnRfcmVhZCIsImRvbWFpbl9yZWFkIiwiaWRlbnRpdHlfcHJvdmlkZXJfcmVhZCIsImR0ci5yb29tcy5yZWFkIiwiZHRyLnJvb21zLndyaXRlIiwiZHRyLmRvY3VtZW50cy5yZWFkIiwiZHRyLmRvY3VtZW50cy53cml0ZSIsImR0ci5wcm9maWxlLnJlYWQiLCJkdHIucHJvZmlsZS53cml0ZSIsImR0ci5jb21wYW55LnJlYWQiLCJkdHIuY29tcGFueS53cml0ZSJdLCJhdWQiOiJmMGYyN2YwZS04NTdkLTRhNzEtYTRkYS0zMmNlY2FlM2E5NzgiLCJhenAiOiJmMGYyN2YwZS04NTdkLTRhNzEtYTRkYS0zMmNlY2FlM2E5NzgiLCJpc3MiOiJodHRwczovL2FjY291bnQtZC5kb2N1c2lnbi5jb20vIiwic3ViIjoiZjRiMGM2ODEtYjBjNy00M2ZjLWI2ZTYtYTViMzA3YWM2MjZjIiwiYXV0aF90aW1lIjoxNTg4OTEyNzc2LCJwd2lkIjoiNTIwZDExNDctZDYwMC00YWNmLWE1NGUtY2Q2MWUyYzYxZTdlIn0.fiUEyHrx9KFb4dhs3ncvLw0-69vyUWeMyOAxoVmTBXb08TF7PASn1KnMTbi1U0nMWkd9NjUwGiH4QCAlyC4X9GxWlHQerHb5ixJ_-3fJyCY2-RxpgAEYQWUuUKmKVfPG3Po-6TyGx1Tf9OPHOeqDJTU70zypFLVhiq5nAVTr_ApEljolDesNn7YtmabhQkjJYxoY-zPaWGLO6o6xnMVHaSFFykXfKc-77Z_-JkNZS69jfdITZ4evcDmOaq_GbtvGQ1dqhRr9fd7I_P1vvgSu8HORzB73x-zHD6QPwkMjGGUUZ9YrEw6kxsUfAHwyPhfkd-AYRwqw1DdM6x9RTCL5tQ";
	    Long tokenExpirationSeconds = 8 * 60 * 60L;
	    String accountId = "10403133";
	    String baseUrl = "http://localhost:8080";

	    
	    String authenticationMethod = "None"; 
	    
	    
	    String basePath = "https://demo.docusign.net/restapi";

	    
	    String docPdf = "Eviction-Notice-California.pdf";
	    
//	    byte[] buffer = readFile(docPdf);
//        String docBase64 = new String(Base64.encode(buffer));
        
//        Document document = new Document();
//        document.setDocumentBase64(docBase64);
//        document.setName("Demo document");
//        document.setFileExtension("pdf");
//        document.setDocumentId("1");
        
       
	    Document document = getDoc(formType);
        Signer signer = createSigner(principal);
        
        
        SignHere signHere = new SignHere();
        signHere.setDocumentId("1");
        signHere.setPageNumber("1");
        signHere.setRecipientId("1");
        signHere.setTabLabel("SignHereTab");
        signHere.anchorString("Signature");
        signHere.anchorYOffset("-15");
        
        
        
        Tabs signerTabs = new Tabs();
        signerTabs.setSignHereTabs(Arrays.asList(signHere));
        signer.setTabs(signerTabs);
//        
        
        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
//        envelopeDefinition.setTemplateId("890050f2-9643-4064-b10d-6c1cee844745");
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
        viewRequest.setEmail(signer.getEmail());
        viewRequest.setUserName(signer.getName());
        viewRequest.setClientUserId(signer.getClientUserId());
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
