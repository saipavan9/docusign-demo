package demo.payment;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import demo.data.ChargeRepository;
import demo.model.ChargeRequest;
import demo.model.Charges;

@Controller
public class PaymentController {

	
	@Autowired
	private ChargeRepository chargeRepo;
	
    @Value("pk_test_mAnugn9yIG8s2vadUs1NH4xz00r9eaMyZi")
    private String stripePublicKey;
 
    @GetMapping("/checkout")
    public String checkout(Model model,HttpSession session, @RequestParam(value="event", required = false) String event) {
    	
    	System.out.println(event);

    	if(!event.equals("signing_complete")) {
    		
    		model.addAttribute("error","Signing has been cancelled/voided");
    		
    		return "home";
    	}
    	Charges ch = chargeRepo.findBydocName((String)session.getAttribute("type"));
    	System.out.println(session.getAttribute("type"));
        model.addAttribute("amount", ch.getPrice() * 100); // in cents
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", ChargeRequest.Currency.USD);
        model.addAttribute("tax",(ch.getPrice()*0.029)+0.3);
        
       
        
        return "checkout";
    }
}
