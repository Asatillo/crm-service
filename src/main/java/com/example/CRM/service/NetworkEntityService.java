package com.example.CRM.service;

import com.example.CRM.exceptions.ExistingResourceException;
import com.example.CRM.exceptions.ResourceNotFoundException;
import com.example.CRM.model.Customer;
import com.example.CRM.model.NetworkEntity;
import com.example.CRM.model.enums.DeviceType;
import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.request.NetworkEntityRequest;
import com.example.CRM.payload.PagedResponse;
import com.example.CRM.payload.request.NetworkEntitySellRequest;
import com.example.CRM.repository.CustomerRepository;
import com.example.CRM.repository.NetworkEntityRepository;
import com.example.CRM.utils.AppUtils;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class NetworkEntityService {

    final NetworkEntityRepository networkEntityRepository;
    final CustomerRepository customerRepository;
    public NetworkEntityService(NetworkEntityRepository networkEntityRepository, CustomerRepository customerRepository) {
        this.networkEntityRepository = networkEntityRepository;
        this.customerRepository = customerRepository;
    }

    public PagedResponse<NetworkEntity> getAll(int page, Integer size, String sort, String search) {
        AppUtils.validatePaginationRequestParams(page, size, sort, NetworkEntity.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<NetworkEntity> networkEntities = networkEntityRepository.findAllWithSearch(search, pageable);
        PagedResponse<NetworkEntity> pagedResponse = new PagedResponse<>(networkEntities);

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

        return pagedResponse;
    }

    public PagedResponse<NetworkEntity> getAllByDeviceType(DeviceType deviceType, int page, int size, String sort) {
        AppUtils.validatePaginationRequestParams(page, size, sort, NetworkEntity.class);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
        Page<NetworkEntity> networkEntities = networkEntityRepository.findAllByDeviceType(deviceType, pageable);
        PagedResponse<NetworkEntity> pagedResponse = new PagedResponse<>(networkEntities);

        AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

        return pagedResponse;
    }

    public PagedResponse<NetworkEntity> getAllByOwnerId(Long ownerId, int page, Integer size, String sort, String search) {
        networkEntityRepository.findById(ownerId).orElseThrow(() -> new ResourceNotFoundException("NetworkEntity", "id", ownerId));
        if(size == -1){
            List<NetworkEntity> networkEntities = networkEntityRepository.findAllByOwnerId(ownerId, search);
            return new PagedResponse<>(networkEntities, 1, networkEntities.size(), networkEntities.size(), 1);
        }else {
            AppUtils.validatePaginationRequestParams(page, size, sort, NetworkEntity.class);
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
            Page<NetworkEntity> networkEntities = networkEntityRepository.findAllByOwnerId(ownerId, search, pageable);
            PagedResponse<NetworkEntity> pagedResponse = new PagedResponse<>(networkEntities);

            AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

            return pagedResponse;
        }
    }

    public PagedResponse<NetworkEntity> getAllAvailableByDeviceType(int page, Integer size, String sort, String search, DeviceType deviceType) {
        AppUtils.validatePaginationRequestParams(page, size, sort, NetworkEntity.class);

        if(size == -1){
            List<NetworkEntity> networkEntities = networkEntityRepository.findAllAvailableByDeviceType(deviceType, search);
            return new PagedResponse<>(networkEntities, 0, networkEntities.size(), networkEntities.size(), 1);
        }else{
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
            Page<NetworkEntity> networkEntities = networkEntityRepository.findAllAvailableByDeviceType(deviceType, search, pageable);
            PagedResponse<NetworkEntity> pagedResponse = new PagedResponse<>(networkEntities);

            AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

            return pagedResponse;
        }
    }

    public ResponseEntity<NetworkEntity> getById(Long id) {
        NetworkEntity networkEntity = networkEntityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("NetworkEntity", "id", id));
        return new ResponseEntity<>(networkEntity, HttpStatus.OK);
    }

    public ResponseEntity<NetworkEntity> addNetworkEntity(@NonNull NetworkEntityRequest networkEntityRequest) {
        if(networkEntityRepository.existsByNetworkIdentifier(networkEntityRequest.getNetworkIdentifier())){
            throw new ExistingResourceException(new ApiResponse(false, String.format(
                    "Network Entity with network identifier %s already exists", networkEntityRequest.getNetworkIdentifier()
            )));
        }
        Customer owner = null;
        if(networkEntityRequest.getOwnerId() != null){
            owner = customerRepository.findById(networkEntityRequest.getOwnerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", networkEntityRequest.getOwnerId()));
        }
        NetworkEntity networkEntity = new NetworkEntity(networkEntityRequest.getNetworkIdentifier(),
                networkEntityRequest.getDeviceType(), owner, networkEntityRequest.getTag());

        return new ResponseEntity<>(networkEntityRepository.save(networkEntity), HttpStatus.CREATED);
    }

    public ResponseEntity<NetworkEntity> updateNetworkEntity(Long id, @NonNull NetworkEntityRequest networkEntityRequest) {
        NetworkEntity existingNetworkEntity = networkEntityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NetworkEntity", "id", id));

        if(!existingNetworkEntity.getNetworkIdentifier().equals(networkEntityRequest.getNetworkIdentifier())){
            existingNetworkEntity.setNetworkIdentifier(networkEntityRequest.getNetworkIdentifier());
        }

        if(!existingNetworkEntity.getDeviceType().equals(networkEntityRequest.getDeviceType())){
            existingNetworkEntity.setDeviceType(networkEntityRequest.getDeviceType());
        }

        if(networkEntityRequest.getTag() == null){
            existingNetworkEntity.setTag(null);
        } else if(!existingNetworkEntity.getTag().equals(networkEntityRequest.getTag())){
            existingNetworkEntity.setTag(networkEntityRequest.getTag());
        }

        if(networkEntityRequest.getOwnerId() == null){
            existingNetworkEntity.setOwner(null);
            existingNetworkEntity.setActive(false);
            existingNetworkEntity.setTag("");
        } else if(!existingNetworkEntity.getOwner().getId().equals(networkEntityRequest.getOwnerId())){
            Customer newOwner = customerRepository.findById(networkEntityRequest.getOwnerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", networkEntityRequest.getOwnerId()));
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

    public PagedResponse<NetworkEntity> getAllByOwnerIdAndDeviceType(Long id, DeviceType deviceType, int page, Integer size,
                                                                     String sort, String search) {
        AppUtils.validatePaginationRequestParams(page, size, sort, NetworkEntity.class);

        if(size == -1){
            List<NetworkEntity> networkEntities = networkEntityRepository.findAllByOwnerIdAndDeviceType(id, deviceType, search);
            return new PagedResponse<>(networkEntities, 0, networkEntities.size(), networkEntities.size(), 1);
        }else{
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sort);
            Page<NetworkEntity> networkEntities = networkEntityRepository.findAllByOwnerIdAndDeviceType(id, deviceType, search, pageable);
            PagedResponse<NetworkEntity> pagedResponse = new PagedResponse<>(networkEntities);

            AppUtils.validatePageNumberLessThanTotalPages(page, pagedResponse.getTotalPages(), pagedResponse.getTotalElements());

            return pagedResponse;
        }
    }

    public ResponseEntity<ApiResponse> assignNetworkEntity(Long id, NetworkEntitySellRequest networkEntitySellRequest) {
        NetworkEntity existingNetworkEntity = networkEntityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NetworkEntity", "id", id));

        if(!existingNetworkEntity.isActive()){
            return new ResponseEntity<>(new ApiResponse(false, "NetworkEntity is inactive"), HttpStatus.BAD_REQUEST);
        }

        if(existingNetworkEntity.getOwner() != null){
            return new ResponseEntity<>(new ApiResponse(false, "NetworkEntity is already owned by the same customer"), HttpStatus.BAD_REQUEST);
        }

        Customer newOwner = customerRepository.findById(networkEntitySellRequest.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", networkEntitySellRequest.getOwnerId()));

        existingNetworkEntity.setOwner(newOwner);
        existingNetworkEntity.setTag(networkEntitySellRequest.getTag());

        networkEntityRepository.save(existingNetworkEntity);
        return new ResponseEntity<>(new ApiResponse(true, "Network Entity assigned successfully"), HttpStatus.OK);
    }
}
