package pl.dawidsowa.mtabd.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.dawidsowa.mtabd.domain.Message;
import pl.dawidsowa.mtabd.service.dto.MessageDTO;

/**
 * Mapper for the entity Message and its DTO MessageDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ChatMapper.class})
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "chat.id", target = "chatId")
    MessageDTO toDto(Message message);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "chatId", target = "chat")
    Message toEntity(MessageDTO messageDTO);

    default Message fromId(Long id) {
        if (id == null) {
            return null;
        }
        Message message = new Message();
        message.setId(id);
        return message;
    }
}
