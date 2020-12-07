package com.dubaleko.vetclinic.service;

import com.dubaleko.vetclinic.entity.ServiceType;
import com.dubaleko.vetclinic.repository.ServiceRepository;
import com.dubaleko.vetclinic.repository.ServiceTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceForService {

    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceRepository serviceRepository;

    public ServiceForService(ServiceTypeRepository serviceTypeRepository, ServiceRepository serviceRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
        this.serviceRepository = serviceRepository;
    }

    public Page getServices(int page, Optional<String> type){
        String typeId = "";
        if (!type.isEmpty()) {
            if (!type.get().equals("Все услуги")) {
                List<ServiceType> serviceTypes = serviceTypeRepository.findAll();
                for (ServiceType serviceType : serviceTypes) {
                    if (serviceType.getServiceTypeName().equals(type.get())) {
                        typeId = serviceType.getId().toString();
                    }
                }
                return serviceRepository.findServiceByPage(typeId, PageRequest.of(page - 1, 19, Sort.Direction.ASC, "serviceName"));
            }
        }
        return serviceRepository.findAllServiceByPage(PageRequest.of(page-1,19, Sort.Direction.ASC,"serviceName"));
    }
}
