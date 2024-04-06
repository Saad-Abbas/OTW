package com.example.otwAppservice.service.countryService;

import com.example.otwAppservice.entity.Country;
import com.example.otwAppservice.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public List<Country> getAllActiveCountries() {
        return countryRepository.findByIsActive(1);

    }


}
