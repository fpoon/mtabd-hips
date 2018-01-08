package pl.dawidsowa.mtabd.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.dawidsowa.mtabd.domain.Chat;
import pl.dawidsowa.mtabd.service.dto.ChatDTO;

/**
 * Mapper for the entity Chat and its DTO ChatDTO.
 */
@Mapper(componentModel = "spring", uses = {ServiceMapper.class})
public interface ChatMapper extends EntityMapper<ChatDTO, Chat> {

    @Mapping(source = "service.id", target = "serviceId")
    ChatDTO toDto(Chat chat);

    @Mapping(source = "serviceId", target = "service")
    @Mapping(target = "messages", ignore = true)
    Chat toEntity(ChatDTO chatDTO);

    default Chat fromId(Long id) {
        if (id == null) {
            return null;
        }
        Chat chat = new Chat();
        chat.setId(id);
        return chat;
    }
}
