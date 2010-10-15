package com.springone.petclinic.web;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import com.springone.petclinic.domain.Visit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@RooWebScaffold(path = "visits", formBackingObject = Visit.class)
@RequestMapping("/visits")
@Controller
public class VisitController {
}
