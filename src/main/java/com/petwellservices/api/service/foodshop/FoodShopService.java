package com.petwellservices.api.service.foodshop;

import java.util.List;

import org.springframework.stereotype.Service;

import com.petwellservices.api.entities.Area;
import com.petwellservices.api.entities.City;
import com.petwellservices.api.entities.FoodShop;
import com.petwellservices.api.entities.FoodShop.Status;
import com.petwellservices.api.exception.ResourceNotFoundException;
import com.petwellservices.api.repository.FoodShopRepository;
import com.petwellservices.api.request.CreateFoodShopRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FoodShopService implements IFoodShopService {
    private final FoodShopRepository foodShopRepository;

    @Override
    public FoodShop createFoodShop(CreateFoodShopRequest foodShop,City city,Area area) {
        FoodShop newFoodShop = new FoodShop();
        newFoodShop.setShopName(foodShop.getShopName());
        newFoodShop.setShopName(foodShop.getShopName());
        newFoodShop.setShopRegistrationId(foodShop.getShopRegistrationId());
        newFoodShop.setRating(foodShop.getRating());

        newFoodShop.setCity(city);
        newFoodShop.setArea(area);
        newFoodShop.setShopPhoneNo(foodShop.getShopPhoneNo());
        newFoodShop.setShopAddress(foodShop.getShopAddress());
        newFoodShop.setStatus(Status.valueOf(foodShop.getStatus()));
        return foodShopRepository.save(newFoodShop);
    }

    @Override
    public List<FoodShop> getAllFoodShops() {
        return foodShopRepository.findAll();
    }

    @Override
    public FoodShop getFoodShopById(Long shopId) {
        return foodShopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Food shop not found with id: " + shopId));
    }

    @Override
    public void deleteFoodShopById(Long shopId) {
        if (!foodShopRepository.existsById(shopId)) {
            throw new ResourceNotFoundException("Food shop not found with id: " + shopId);
        }
        foodShopRepository.deleteById(shopId);
    }

    @Override
    public List<FoodShop> getFoodShopsByCityId(Long cityId) {
        return foodShopRepository.findByCityCityId(cityId);
    }

    @Override
    public List<FoodShop> getFoodShopsByAreaId(Long areaId) {
        return foodShopRepository.findByAreaAreaId(areaId);
    }
}
