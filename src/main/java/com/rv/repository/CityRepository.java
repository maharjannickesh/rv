package com.rv.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.rv.entity.CityEntity;


@Repository
public interface CityRepository extends JpaRepository<CityEntity, Integer> {

	List<CityEntity> findAllCityNameByStateEntityAbbreviation(String abb);

	CityEntity findOneByName(String cityName);

	List<CityEntity> findAllByName(String cityName);
	
	
	
}
