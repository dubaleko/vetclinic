package com.dubaleko.vetclinic.service;

import com.dubaleko.vetclinic.repository.ClinicRepository;
import com.dubaleko.vetclinic.repository.ServiceRepository;
import com.dubaleko.vetclinic.repository.ServiceTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceForService {

    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceRepository serviceRepository;
    private final ClinicRepository clinicRepository;

    public ServiceForService(ServiceTypeRepository serviceTypeRepository, ServiceRepository serviceRepository,
                             ClinicRepository clinicRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
        this.serviceRepository = serviceRepository;
        this.clinicRepository = clinicRepository;
    }

    public Page getServices(int page, Optional<Long> typeId, Optional<Long> clinicId){
        if (typeId.isPresent() && clinicId.isPresent()){
            return serviceRepository.findServicesByClinicAndServiceType(clinicRepository.getClinicById(clinicId.get()),
                    serviceTypeRepository.getServiceTypeById(typeId.get()), PageRequest.of(page - 1, 19,
                            Sort.Direction.ASC, "serviceName"));
        }
        else if (clinicId.isPresent()){
            return serviceRepository.findServiceByClinic(clinicRepository.getClinicById(clinicId.get()),
                    PageRequest.of(page - 1, 19, Sort.Direction.ASC, "serviceName"));
        }
        else if (typeId.isPresent()){
            return serviceRepository.findServiceByServiceType(serviceTypeRepository.getServiceTypeById(typeId.get()),
                    PageRequest.of(page - 1, 19, Sort.Direction.ASC, "serviceName"));
        }
        return serviceRepository.findAllServiceByPage(PageRequest.of(page - 1, 19,
                Sort.Direction.ASC, "serviceName"));
    }
}