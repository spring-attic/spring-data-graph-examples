package com.springone.myrestaurants.web;

import javax.validation.Valid;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springone.myrestaurants.domain.Restaurant;

@RequestMapping("/restaurants")
@Controller
public class RestaurantController {

	@RequestMapping(method = RequestMethod.POST)
    public String create(@Valid Restaurant restaurant, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("restaurant", restaurant);
            return "restaurants/create";
        }
        restaurant.persist();
        return "redirect:/restaurants/" + restaurant.getId();
    }

	@RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        return "restaurants/create";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("restaurant", Restaurant.findRestaurant(id));
        model.addAttribute("itemId", id);
        return "restaurants/show";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            model.addAttribute("restaurants", Restaurant.findRestaurantEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Restaurant.countRestaurants() / sizeNo;
            model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            model.addAttribute("restaurants", Restaurant.findAllRestaurants());
        }
        return "restaurants/list";
    }

	@RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid Restaurant restaurant, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("restaurant", restaurant);
            return "restaurants/update";
        }
        restaurant.merge();
        return "redirect:/restaurants/" + restaurant.getId();
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("restaurant", Restaurant.findRestaurant(id));
        return "restaurants/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        Restaurant.findRestaurant(id).remove();
        model.addAttribute("page", (page == null) ? "1" : page.toString());
        model.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/restaurants?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }

	Converter<Restaurant, String> getRestaurantConverter() {
        return new Converter<Restaurant, String>() {
            public String convert(Restaurant restaurant) {
                return new StringBuilder().append(restaurant.getName()).append(" ").append(restaurant.getCity()).append(" ").append(restaurant.getState()).toString();
            }
        };
    }

	@InitBinder
    void registerConverters(WebDataBinder binder) {
        if (binder.getConversionService() instanceof GenericConversionService) {
            GenericConversionService conversionService = (GenericConversionService) binder.getConversionService();
            conversionService.addConverter(getRestaurantConverter());
        }
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public String showJson(@PathVariable("id") Long id) {
        return Restaurant.findRestaurant(id).toJson();
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        Restaurant.fromJsonToRestaurant(json).persist();
        return new ResponseEntity<String>("Restaurant created", HttpStatus.CREATED);
    }

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public String listJson() {
        return Restaurant.toJsonArray(Restaurant.findAllRestaurants());
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (Restaurant restaurant: Restaurant.fromJsonArrayToRestaurants(json)) {
            restaurant.persist();
        }
        return new ResponseEntity<String>("Restaurant created", HttpStatus.CREATED);
    }
}
