package com.ead.payment.specifications;

import com.ead.payment.models.Payment;
import com.ead.payment.models.User;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {

    @And({
            @Spec(path = "paymentControl", spec = Equal.class),
            @Spec(path = "valuePaid", spec = Equal.class),
            @Spec(path = "lastDigitsCreditCard", spec = Like.class),
            @Spec(path = "paymentMessage", spec = Like.class)})
    public interface PaymentSpec extends Specification<Payment>{
    }

    public static Specification<Payment> paymentUserId(final UUID userId){
        return (root, query, cb) ->{
            query.distinct(true);
            Root<Payment> payment = root;
            Root<User> user = query.from(User.class);
            Expression<Collection<Payment>> usersPayments = user.get("payments");
            return cb.and(cb.equal(user.get("id"), userId), cb.isMember(payment, usersPayments));
        };
    }
}
