package demo.docusign;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
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

import demo.data.UserRepository;
import demo.model.User;

@Controller
public class DemoController {

	

	@Autowired
	  private static UserRepository userRepo;
	
	
	public DemoController(UserRepository userRepo) {
		DemoController.userRepo = userRepo;
		
	}
	  
	@Value("${${docusign.baseurl}}")
	private String baseUrl;
	
	@Value("${docusign.oauth.accesstoken}") 
	String accessToken;

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
		    }else if(formType.equals("no_lease")) {
		    	docPdf = "lease_renewal.pdf";
		    	name = "Lease Termination";
		    }
		    else if(formType.equals("violence")) {
		    	docPdf = "violence.pdf";
		    	name = "Lease Termination Due to Violence";
		    }
		   else {
		    	  docPdf = "Eviction-Notice-California.pdf";
		    	  name = "Other";
		    }
		  
		    byte[] buffer = readFile(docPdf);
	        String docBase64 = new String(Base64.encode(buffer));
	        
	        Document document = new Document();
	        document.setDocumentBase64(docBase64);
	        document.setName(name+" Document");
	        document.setFileExtension("pdf");
	        document.setDocumentId("1");
	        
	        return document;
	        
	}
	
	
	@PostMapping("/sign")
	public Object create(HttpServletRequest request,Principal principal) throws IOException,ApiException {
	    Long tokenExpirationSeconds = 8 * 60 * 60L;
	    String accountId = "10403133";
	    String authenticationMethod = "None"; 
	    
	    
	    String basePath = "https://demo.docusign.net/restapi";
	           
	    String formType = request.getParameter("inlineRadioOptions");
	    
	    Document document = getDoc(formType);
        Signer signer = createSigner(principal);
        
        request.getSession().setAttribute("type", document.getName());
        
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
//
//        viewRequest.setReturnUrl("http://35.193.30.60:8080/checkout");
        viewRequest.setReturnUrl("http://localhost:8080/checkout");
        viewRequest.setAuthenticationMethod(authenticationMethod);
        viewRequest.setEmail(signer.getEmail());
        viewRequest.setUserName(signer.getName());
        viewRequest.setClientUserId(signer.getClientUserId());
        // call the CreateRecipientView API
        ViewUrl results1 = envelopesApi.createRecipientView(accountId, envelopeId, viewRequest);
        
        request.getSession().setAttribute("envelopId", envelopeId);
        
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
