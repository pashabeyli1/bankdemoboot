package az.orient.bankdemoboot.dto.request;

import az.orient.bankdemoboot.entity.Account;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
public class ReqTransaction {

    private Long dtAccountId;
    private String  crAccount;
    private Double amount;
    private Double commission;
    private String currency;

}
