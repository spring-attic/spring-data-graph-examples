package com.springone.myrestaurants.web;

import java.util.Collection;

import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springone.myrestaurants.domain.Restaurant;
import com.springone.myrestaurants.domain.UserAccount;

@RequestMapping("/useraccounts")
@Controller
public class SignUpController {

	@RequestMapping(method = RequestMethod.POST)
    public String create(@Valid UserAccount userAccount, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("userAccount", userAccount);
            addDateTimeFormatPatterns(model);
            return "useraccounts/create";
        }
        userAccount.persist();
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
        model.addAttribute("useraccount", UserAccount.findUserAccount(id));
        model.addAttribute("itemId", id);
        return "useraccounts/show";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            model.addAttribute("useraccounts", UserAccount.findUserAccountEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) UserAccount.countUserAccounts() / sizeNo;
            model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            model.addAttribute("useraccounts", UserAccount.findAllUserAccounts());
        }
        addDateTimeFormatPatterns(model);
        return "useraccounts/list";
    }

	@RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid UserAccount userAccount, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("userAccount", userAccount);
            addDateTimeFormatPatterns(model);
            return "useraccounts/update";
        }
        userAccount.merge();
        return "redirect:/useraccounts/" + userAccount.getId();
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("userAccount", UserAccount.findUserAccount(id));
        addDateTimeFormatPatterns(model);
        return "useraccounts/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        UserAccount.findUserAccount(id).remove();
        model.addAttribute("page", (page == null) ? "1" : page.toString());
        model.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/useraccounts?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }

	@ModelAttribute("restaurants")
    public Collection<Restaurant> populateRestaurants() {
        return Restaurant.findAllRestaurants();
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

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public String showJson(@PathVariable("id") Long id) {
        return UserAccount.findUserAccount(id).toJson();
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        UserAccount.fromJsonToUserAccount(json).persist();
        return new ResponseEntity<String>("UserAccount created", HttpStatus.CREATED);
    }

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public String listJson() {
        return UserAccount.toJsonArray(UserAccount.findAllUserAccounts());
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (UserAccount useraccount: UserAccount.fromJsonArrayToUserAccounts(json)) {
            useraccount.persist();
        }
        return new ResponseEntity<String>("UserAccount created", HttpStatus.CREATED);
    }
}
