package demo.payment;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import demo.model.ChargeRequest;
import demo.model.ChargeRequest.Currency;

@Controller
public class ChargeController {
 
    @Autowired
    private StripeService paymentsService;
 
    @PostMapping("/charge")
    public String charge(ChargeRequest chargeRequest, Model model,HttpServletRequest request)
      throws StripeException {
        chargeRequest.setDescription("Signing charge");
        chargeRequest.setCurrency(Currency.USD);
        Charge charge = paymentsService.charge(chargeRequest);
        model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("balance_transaction", charge.getBalanceTransaction());
        
        if(charge.getStatus().equals("succeeded")) {
        	request.removeAttribute("type2");
        	request.removeAttribute("type");
        }
        
        return "result";
    }
    
    @GetMapping("/test")
    public String gettest() {return "result";}
 
    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "result";
    }
}