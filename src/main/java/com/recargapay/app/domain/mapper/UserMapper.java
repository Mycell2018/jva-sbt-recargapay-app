package com.recargapay.app.domain.mapper;

import com.recargapay.app.domain.dto.user.UserCreateDTO;
import com.recargapay.app.domain.dto.user.UserReadDTO;
import com.recargapay.app.domain.dto.user.UserUpdateDTO;
import com.recargapay.app.domain.model.User;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User createToEntity(UserCreateDTO dto);

    User updateToEntity(UserUpdateDTO dto);

    User readToEntity(UserReadDTO dto);

    UserReadDTO entityToRead(User entity);

    List<UserReadDTO> listEntityToRead(List<User> entities);
}
