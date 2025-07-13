package com.recargapay.app.domain.mapper;

import com.recargapay.app.domain.dto.bankaccount.TransactionReadDTO;
import com.recargapay.app.domain.model.Transaction;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionReadDTO entityToRead(Transaction entity);

    List<TransactionReadDTO> listEntityToRead(List<Transaction> entities);
}
