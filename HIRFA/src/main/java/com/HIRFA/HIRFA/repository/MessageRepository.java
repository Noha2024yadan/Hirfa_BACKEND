package com.HIRFA.HIRFA.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HIRFA.HIRFA.entity.Message;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByCooperativeSender_CooperativeIdAndDesignerReceiver_DesignerId(UUID coopId, UUID designerId);

    List<Message> findByDesignerSender_DesignerIdAndCooperativeReceiver_CooperativeId(UUID designerId, UUID coopId);
}
