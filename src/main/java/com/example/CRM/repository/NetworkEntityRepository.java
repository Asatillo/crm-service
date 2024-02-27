package com.example.CRM.repository;

import com.example.CRM.model.NetworkEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NetworkEntityRepository extends JpaRepository<NetworkEntity, Long> {

    Page<NetworkEntity> findAllByDeviceType(String deviceType, Pageable pageable);

    @Query("SELECT n FROM NetworkEntity n " +
            "WHERE n.owner.id = ?1 AND (n.networkIdentifier LIKE %?2%)")
    Page<NetworkEntity> findAllByOwnerId(Long ownerId, String search, Pageable pageable);

    @Query("SELECT n FROM NetworkEntity n " +
            "WHERE n.owner.id = ?1 AND n.deviceType = ?2 AND (n.networkIdentifier LIKE %?3%)" +
            "AND NOT EXISTS (SELECT s FROM Subscription s WHERE s.networkEntity.id = n.id AND s.isActive = true)")
    List<NetworkEntity> findAllByOwnerIdAndDeviceType(Long id, String deviceType, String search);

    @Query("SELECT n FROM NetworkEntity n " +
            "WHERE n.owner.id = ?1 AND n.deviceType = ?2 AND (n.networkIdentifier LIKE %?3%)" +
            "AND NOT EXISTS (SELECT s FROM Subscription s WHERE s.networkEntity.id = n.id AND s.isActive = true)")
    Page<NetworkEntity> findAllByOwnerIdAndDeviceType(Long id, String deviceType, String search, Pageable pageable);

    @Query("SELECT n FROM NetworkEntity n " +
            "WHERE n.deviceType = ?1 AND n.owner IS NULL AND (n.networkIdentifier LIKE %?2%)")
    List<NetworkEntity> findAllAvailableByDeviceType(String deviceType, String search);

    @Query("SELECT n FROM NetworkEntity n " +
            "WHERE n.deviceType = ?1 AND n.owner IS NULL AND (n.networkIdentifier LIKE %?2%)")
    Page<NetworkEntity> findAllAvailableByDeviceType(String deviceType, String search,Pageable pageable);
}
