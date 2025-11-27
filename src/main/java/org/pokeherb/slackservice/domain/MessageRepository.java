package org.pokeherb.slackservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * 슬랙 메시지 전송 이력 저장 및 조회 Repository
 */
public interface MessageRepository extends JpaRepository<Message, UUID> {

}
