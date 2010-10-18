package com.springone.petclinic.domain;

import org.springframework.datastore.graph.annotation.NodeEntity;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.entity.RooEntity;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import com.springone.petclinic.reference.Specialty;
import javax.persistence.Enumerated;

@NodeEntity
@RooToString
@RooJavaBean
@RooEntity
public class Vet {

    @Size(min = 3, max = 30)
    private String firstName;

    @NotNull
    @Size(min = 3, max = 30)
    private String lastName;

    @Enumerated
    private Specialty specialty;

    @NotNull
    private String telephone;

    @Size(max = 30)
    private String homePage;

    @Size(min = 6, max = 30)
    private String email;
}
