package com.dubaleko.vetclinic.service;

import com.dubaleko.vetclinic.entity.ServiceType;
import com.dubaleko.vetclinic.repository.ServiceRepository;
import com.dubaleko.vetclinic.repository.ServiceTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceForService {

    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceRepository serviceRepository;

    public ServiceForService(ServiceTypeRepository serviceTypeRepository, ServiceRepository serviceRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
        this.serviceRepository = serviceRepository;
    }

    public Page getServices(int page, String type){
        Long typeId = 0L;
        if (type.equals("Все услуги") || type.equals("")) {
            return serviceRepository.findAllServiceByPage(PageRequest.of(page - 1, 19, Sort.Direction.ASC, "serviceName"));
        }
        else {
            List<ServiceType> serviceTypes = serviceTypeRepository.findAll();
            for (ServiceType serviceType : serviceTypes) {
                if (serviceType.getServiceTypeName().equals(type)) {
                    typeId = serviceType.getId();
                }
            }
            return serviceRepository.findServiceByPage(typeId, PageRequest.of(page - 1, 19, Sort.Direction.ASC, "serviceName"));
        }
    }
}