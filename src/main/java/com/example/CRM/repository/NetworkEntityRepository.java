package com.example.CRM.repository;

import com.example.CRM.model.NetworkEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NetworkEntityRepository extends JpaRepository<NetworkEntity, Long> {

    Page<NetworkEntity> findAllByDeviceType(String deviceType, Pageable pageable);

    Page<NetworkEntity> findAllByOwnerId(Long ownerId, Pageable pageable);
}
