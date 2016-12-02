package com.rv.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rv.dto.UserCityDTO;
import com.rv.entity.CityEntity;
import com.rv.entity.UserEntity;
import com.rv.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CityService cityService;

	@Autowired
	private StateService stateService;

	@Override
	public UserEntity getUserByFirstName(String firstName) {
		return userRepository.findOneByFirstName(firstName);
	}

	@Override
	public UserEntity saveNewCityVisited(UserEntity userEntity) {
		return userRepository.save(userEntity);
	}

	@Override
	public UserEntity saveUser(UserEntity userEntity) {
		return userRepository.save(userEntity);
	}

	@Override
	public UserEntity insertVisitedCity(UserCityDTO userCityDTO, String user) {
		if (cityService.validateCity(userCityDTO.getCity()) && stateService.verifyState(userCityDTO.getState())
				&& cityService.validateCityAndState(userCityDTO.getCity(), userCityDTO.getState())) {
			UserEntity userEntity = getUserByFirstName(user);
			CityEntity cityEntity = cityService.getCityByNameAndState(userCityDTO.getCity(), userCityDTO.getState());

			Set<CityEntity> userCities = userEntity.getCities();
			userCities.add(cityEntity);
			userEntity.setCities(userCities);

			return saveNewCityVisited(userEntity);

		}

		return null;
	}

	@Override
	public List<String> visitedCity(String user) {
		UserEntity userEntity = getUserByFirstName(user);
		Set<CityEntity> userCities = userEntity.getCities();
		List<String> cities = userCities.stream().map(c -> c.getName()).collect(Collectors.toList());
		return cities;
	}

	@Override
	public Set<String> visitedState(String user) {
		UserEntity userEntity = getUserByFirstName(user);
		Set<CityEntity> userCities = userEntity.getCities();
		Set<String> state = userCities.stream().map(c -> c.getStateEntity().getName()).collect(Collectors.toSet());
		return state;
	}

	@Override
	public Set<CityEntity> deleteVisitedCity(String user, String city, String state) {
		UserEntity userEntity = getUserByFirstName(user);
		Set<CityEntity> cities = userEntity.getCities();
		Iterator<CityEntity> itr = cities.iterator();
		while (itr.hasNext()) {
			CityEntity cityNext = itr.next();
			if (cityNext.getName().equals(city) && cityNext.getStateEntity().getAbbreviation().equals(state)) {
				cities.remove(cityNext);
			}
		}
		userEntity.setCities(cities);
		return saveUser(userEntity).getCities();
	}

}
