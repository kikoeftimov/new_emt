package new_emt.demo.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import new_emt.demo.model.dto.ChargeRequest;

public interface PaymentService {

    Charge charge(ChargeRequest chargeRequest) throws StripeException;
}
