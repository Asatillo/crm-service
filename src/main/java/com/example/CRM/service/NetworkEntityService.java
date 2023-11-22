package com.example.CRM.service;

import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.model.Customer;
import com.example.CRM.model.NetworkEntity;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.NetworkEntityRequest;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.repository.CustomerRepository;
import com.example.CRM.repository.NetworkEntityRepository;
import com.example.CRM.utils.AppUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class NetworkEntityService {

    final NetworkEntityRepository networkEntityRepository;
    final CustomerRepository customerRepository;
    public NetworkEntityService(NetworkEntityRepository networkEntityRepository, CustomerRepository customerRepository) {
        this.networkEntityRepository = networkEntityRepository;
        this.customerRepository = customerRepository;
    }

    public PagedResponse<NetworkEntity> getAll(int page, Integer size, String sort) {
        AppUtils.validatePaginationRequestParams(page, size, sort, NetworkEntity.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<NetworkEntity> networkEntities = networkEntityRepository.findAll(pageable);
        PagedResponse<NetworkEntity> pagedResponse = new PagedResponse<>();

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages());

        return pagedResponse.returnPagedResponse(networkEntities);
    }

    public PagedResponse<NetworkEntity> getAllByDeviceType(String deviceType, int page, int size, String sort) {
        AppUtils.validateDeviceType(deviceType);
        AppUtils.validatePaginationRequestParams(page, size, sort, NetworkEntity.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<NetworkEntity> networkEntities = networkEntityRepository.findAllByDeviceType(deviceType, pageable);
        PagedResponse<NetworkEntity> pagedResponse = new PagedResponse<>();

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages());

        return pagedResponse.returnPagedResponse(networkEntities);
    }

    public PagedResponse<NetworkEntity> getAllByOwnerId(Long ownerId, int page, Integer size, String sort) {
        networkEntityRepository.findById(ownerId).orElseThrow(() -> new ResourceNotFoundException("NetworkEntity", "id", ownerId));

        AppUtils.validatePaginationRequestParams(page, size, sort, NetworkEntity.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<NetworkEntity> networkEntities = networkEntityRepository.findAllByOwnerId(ownerId, pageable);
        PagedResponse<NetworkEntity> pagedResponse = new PagedResponse<>();

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages());

        return pagedResponse.returnPagedResponse(networkEntities);
    }

    public ResponseEntity<NetworkEntity> getById(Long id) {
        NetworkEntity networkEntity = networkEntityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("NetworkEntity", "id", id));
        return new ResponseEntity<>(networkEntity, HttpStatus.OK);
    }

    public ResponseEntity<NetworkEntity> addNetworkEntity(NetworkEntityRequest networkEntityRequest) {
        Customer owner = customerRepository.findById(networkEntityRequest.getOwner_id())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", networkEntityRequest.getOwner_id()));

        NetworkEntity networkEntity = new NetworkEntity(networkEntityRequest.getNetworkIdentifier(),
                networkEntityRequest.getDeviceType(), owner, networkEntityRequest.getTag());

        return new ResponseEntity<>(networkEntityRepository.save(networkEntity), HttpStatus.CREATED);
    }

    public ResponseEntity<NetworkEntity> updateNetworkEntity(Long id, NetworkEntityRequest networkEntityRequest) {
        NetworkEntity existingNetworkEntity = networkEntityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NetworkEntity", "id", id));

        if(!existingNetworkEntity.getNetworkIdentifier().equals(networkEntityRequest.getNetworkIdentifier())){
            existingNetworkEntity.setNetworkIdentifier(networkEntityRequest.getNetworkIdentifier());
        }

        if(!existingNetworkEntity.getDeviceType().equals(networkEntityRequest.getDeviceType())){
            existingNetworkEntity.setDeviceType(networkEntityRequest.getDeviceType());
        }

        if(!existingNetworkEntity.getTag().equals(networkEntityRequest.getTag())){
            existingNetworkEntity.setTag(networkEntityRequest.getTag());
        }

        if(!existingNetworkEntity.getOwner().getId().equals(networkEntityRequest.getOwner_id())){
            Customer newOwner = customerRepository.findById(networkEntityRequest.getOwner_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", networkEntityRequest.getOwner_id()));
            existingNetworkEntity.setOwner(newOwner);
        }

        return new ResponseEntity<>(networkEntityRepository.save(existingNetworkEntity), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> deactivateNetworkEntity(Long id) {
        NetworkEntity existingNetworkEntity = networkEntityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NetworkEntity", "id", id));

        if(!existingNetworkEntity.isActive()){
            return new ResponseEntity<>(new ApiResponse(false, "NetworkEntity is already inactive"), HttpStatus.BAD_REQUEST);
        }
        existingNetworkEntity.setActive(false);
        networkEntityRepository.save(existingNetworkEntity);
        return new ResponseEntity<>(new ApiResponse(true, "NetworkEntity deactivated successfully"), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> activateNetworkEntity(Long id) {
        NetworkEntity existingNetworkEntity = networkEntityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NetworkEntity", "id", id));

        if(existingNetworkEntity.isActive()){
            return new ResponseEntity<>(new ApiResponse(false, "NetworkEntity is already active"), HttpStatus.BAD_REQUEST);
        }
        existingNetworkEntity.setActive(true);
        networkEntityRepository.save(existingNetworkEntity);
        return new ResponseEntity<>(new ApiResponse(true, "NetworkEntity activated successfully"), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> deleteNetworkEntity(Long id) {
        networkEntityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("NetworkEntity", "id", id));

        // TODO: check if present in any subscription
        networkEntityRepository.deleteById(id);
        return new ResponseEntity<>(new ApiResponse(true, "NetworkEntity deleted successfully"), HttpStatus.OK);
    }
}
