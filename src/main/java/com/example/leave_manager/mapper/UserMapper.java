package com.example.leave_manager.mapper;
import com.example.leave_manager.dto.UserDto;
import com.example.leave_manager.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto convertEntityToDto(User user);
    User convertDtoToEntity(UserDto userDto);
    UserMapper mapper = Mappers.getMapper(UserMapper.class);
}
