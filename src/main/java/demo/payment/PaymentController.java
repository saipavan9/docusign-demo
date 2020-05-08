package demo.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import demo.model.ChargeRequest;

@Controller
public class PaymentController {

	
	@GetMapping("/pay")
	public String getpay() {
		return "payment";
	}
	
	
    @Value("pk_test_mAnugn9yIG8s2vadUs1NH4xz00r9eaMyZi")
    private String stripePublicKey;
 
    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("amount", 50 * 100); // in cents
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", ChargeRequest.Currency.USD);
        return "payment";
    }
}
