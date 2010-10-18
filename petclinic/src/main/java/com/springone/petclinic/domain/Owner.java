package com.springone.petclinic.domain;

import org.springframework.datastore.graph.annotation.NodeEntity;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.entity.RooEntity;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import java.util.Set;
import org.springframework.datastore.graph.annotation.RelatedTo;
import org.springframework.datastore.graph.api.Direction;
import com.springone.petclinic.domain.Pet;
import javax.persistence.OneToMany;

@NodeEntity
@RooToString
@RooJavaBean
@RooEntity
public class Owner {

    @Size(min = 3, max = 30)
    private String firstName;

    @NotNull
    @Size(min = 3, max = 30)
    private String lastName;

    @NotNull
    @Size(min = 1, max = 50)
    private String address;

    @NotNull
    @Size(max = 30)
    private String city;

    @NotNull
    private String telephone;

    @Size(min = 6, max = 30)
    private String email;

    @RelatedTo(type = "OWNS", direction = Direction.OUTGOING, elementClass = Pet.class)
    @OneToMany(targetEntity = Pet.class)
    private Set<com.springone.petclinic.domain.Pet> pets;
}
