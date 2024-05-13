package com.example.CRM.service;

import com.example.CRM.exceptions.ApiException;
import com.example.CRM.model.NetworkEntity;
import com.example.CRM.model.enums.DeviceType;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.payload.request.NetworkEntityRequest;
import com.example.CRM.repository.CustomerRepository;
import com.example.CRM.repository.NetworkEntityRepository;
import com.example.CRM.service.NetworkEntityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NetworkEntityTest {

    @Mock
    private NetworkEntityRepository networkEntityRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private NetworkEntityService networkEntityService;

    @Test
    public void NetworkEntityService_CreateNetworkEntity_ReturnsNetworkEntity() {
        NetworkEntity networkEntity = new NetworkEntity("+36201234568", DeviceType.MOBILE, "");
        when(networkEntityRepository.save(Mockito.any(NetworkEntity.class))).thenReturn(networkEntity);

        NetworkEntityRequest networkEntityRequest = new NetworkEntityRequest("+36201234568", DeviceType.MOBILE, null, "", true);
        NetworkEntity networkEntity1 = networkEntityService.addNetworkEntity(networkEntityRequest).getBody();

        assertNotNull(networkEntity1);
        assertEquals(networkEntity1.getNetworkIdentifier(), networkEntity.getNetworkIdentifier());
    }

    @Test
    public void NetworkEntityService_GetNetworkEntityById_ReturnsNetworkEntity() {
        NetworkEntity networkEntity = new NetworkEntity("+36201234568", DeviceType.MOBILE, "");
        when(networkEntityRepository.findById(Mockito.any(Long.class))).thenReturn(java.util.Optional.of(networkEntity));

        NetworkEntity networkEntity1 = networkEntityService.getById(1L).getBody();
        Assertions.assertNotNull(networkEntity1);
        Assertions.assertEquals(networkEntity1.getNetworkIdentifier(), networkEntity.getNetworkIdentifier());
    }

    @Test
    public void NetworkEntityService_GetAllNetworkEntities_ReturnsPagedResponse() {
        NetworkEntity networkEntity1 = new NetworkEntity("+36201234568", DeviceType.MOBILE, "");
        NetworkEntity networkEntity2 = new NetworkEntity("+36201234569", DeviceType.MOBILE, "");
        NetworkEntity networkEntity3 = new NetworkEntity("+36201234570", DeviceType.MOBILE, "");
        Page<NetworkEntity> networkEntitiesPage = new PageImpl<>(List.of(networkEntity1, networkEntity2, networkEntity3));
        PagedResponse<NetworkEntity> networkEntityPagedResponse = new PagedResponse<>(networkEntitiesPage);
        when(networkEntityRepository.findAllWithSearch(anyString(), Mockito.any(Pageable.class))).thenReturn(networkEntitiesPage);

        PagedResponse<NetworkEntity> networkEntities = networkEntityService.getAll(0, 10, "id", "");
        assertNotNull(networkEntities);
        assertEquals(networkEntityPagedResponse, networkEntities);
    }
}
