package com.recargapay.app.domain.mapper;

import com.recargapay.app.domain.dto.bankaccount.BankAccountReadDTO;
import com.recargapay.app.domain.model.BankAccount;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    BankAccountReadDTO entityToRead(BankAccount entity);

    List<BankAccountReadDTO> listEntityToRead(List<BankAccount> entities);
}
