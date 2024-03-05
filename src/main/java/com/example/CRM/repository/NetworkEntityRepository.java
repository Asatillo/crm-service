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
            "WHERE n.owner.id = ?1 AND (n.networkIdentifier LIKE %?2%)")
    List<NetworkEntity> findAllByOwnerId(Long ownerId, String search);

    @Query("SELECT n FROM NetworkEntity n " +
            "WHERE n.owner.id = ?1 AND n.deviceType = ?2 AND (n.networkIdentifier LIKE %?3%)")
    List<NetworkEntity> findAllByOwnerIdAndDeviceType(Long id, String deviceType, String search);

    @Query("SELECT n FROM NetworkEntity n " +
            "WHERE n.owner.id = ?1 AND n.deviceType = ?2 AND (n.networkIdentifier LIKE %?3%)")
    Page<NetworkEntity> findAllByOwnerIdAndDeviceType(Long id, String deviceType, String search, Pageable pageable);

    @Query("SELECT n FROM NetworkEntity n " +
            "WHERE n.deviceType = ?1 AND n.owner IS NULL AND (n.networkIdentifier LIKE %?2%)")
    List<NetworkEntity> findAllAvailableByDeviceType(String deviceType, String search);

    @Query("SELECT n FROM NetworkEntity n " +
            "WHERE n.deviceType = ?1 AND n.owner IS NULL AND (n.networkIdentifier LIKE %?2%)")
    Page<NetworkEntity> findAllAvailableByDeviceType(String deviceType, String search,Pageable pageable);

    @Query("SELECT n FROM NetworkEntity n " +
            "WHERE CONCAT(n.networkIdentifier, ' ', n.deviceType, ' ', n.tag) LIKE %?1%")
    Page<NetworkEntity> findAllWithSearch(String search, Pageable pageable);

    @Query("SELECT n FROM NetworkEntity n " +
            "WHERE n.owner IS NOT NULL AND n.networkIdentifier LIKE %?1%")
    List<NetworkEntity> findAllByOwnerNotEmpty(String search);
}
