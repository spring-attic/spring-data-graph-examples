package com.springone.myrestaurants.web;

import java.util.Collection;

import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springone.myrestaurants.dao.RestaurantDao;
import com.springone.myrestaurants.dao.UserAccountDao;
import com.springone.myrestaurants.domain.Restaurant;
import com.springone.myrestaurants.domain.UserAccount;

@RequestMapping("/useraccounts")
@Controller
public class SignUpController {

	@Autowired
	RestaurantDao restaurantDao;

	@Autowired
	UserAccountDao userAccountDao;

	@RequestMapping(method = RequestMethod.POST)
    public String create(@Valid UserAccount userAccount, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("userAccount", userAccount);
            addDateTimeFormatPatterns(model);
            return "useraccounts/create";
        }
        userAccountDao.persist(userAccount);
        return "redirect:/useraccounts/" + userAccount.getId();
    }

	@RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("userAccount", new UserAccount());
        addDateTimeFormatPatterns(model);
        return "useraccounts/create";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model model) {
        addDateTimeFormatPatterns(model);
        model.addAttribute("useraccount", userAccountDao.findUserAccount(id));
        model.addAttribute("itemId", id);
        return "useraccounts/show";
    }

	@RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid UserAccount userAccount, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("userAccount", userAccount);
            addDateTimeFormatPatterns(model);
            return "useraccounts/update";
        }
        userAccountDao.merge(userAccount);
        return "redirect:/useraccounts/" + userAccount.getId();
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("userAccount", userAccountDao.findUserAccount(id));
        addDateTimeFormatPatterns(model);
        return "useraccounts/update";
    }

	@ModelAttribute("restaurants")
    public Collection<Restaurant> populateRestaurants() {
        return restaurantDao.findAllRestaurants();
    }

	Converter<Restaurant, String> getRestaurantConverter() {
        return new Converter<Restaurant, String>() {
            public String convert(Restaurant restaurant) {
                return new StringBuilder().append(restaurant.getName()).append(" ").append(restaurant.getCity()).append(" ").append(restaurant.getState()).toString();
            }
        };
    }

	Converter<UserAccount, String> getUserAccountConverter() {
        return new Converter<UserAccount, String>() {
            public String convert(UserAccount userAccount) {
                return new StringBuilder().append(userAccount.getUserName()).append(" ").append(userAccount.getFirstName()).append(" ").append(userAccount.getLastName()).toString();
            }
        };
    }

	@InitBinder
    void registerConverters(WebDataBinder binder) {
        if (binder.getConversionService() instanceof GenericConversionService) {
            GenericConversionService conversionService = (GenericConversionService) binder.getConversionService();
            conversionService.addConverter(getRestaurantConverter());
            conversionService.addConverter(getUserAccountConverter());
        }
    }

	void addDateTimeFormatPatterns(Model model) {
        model.addAttribute("userAccount_birthdate_date_format", DateTimeFormat.patternForStyle("S-", LocaleContextHolder.getLocale()));
    }
}
