package pl.dawidsowa.mtabd.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.dawidsowa.mtabd.service.dto.ChatDTO;

/**
 * Service Interface for managing Chat.
 */
public interface ChatService {

    /**
     * Save a chat.
     *
     * @param chatDTO the entity to save
     * @return the persisted entity
     */
    ChatDTO save(ChatDTO chatDTO);

    /**
     * Get all the chats.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ChatDTO> findAll(Pageable pageable);

    /**
     * Get the "id" chat.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ChatDTO findOne(Long id);

    /**
     * Delete the "id" chat.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
