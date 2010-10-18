package com.springone.petclinic.domain;

import org.springframework.datastore.graph.annotation.NodeEntity;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.entity.RooEntity;
import javax.validation.constraints.Size;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Set;
import org.springframework.datastore.graph.annotation.RelatedTo;
import org.springframework.datastore.graph.api.Direction;
import com.springone.petclinic.domain.Pet;
import javax.persistence.ManyToOne;
import com.springone.petclinic.domain.Vet;

@NodeEntity
@RooToString
@RooJavaBean
@RooEntity
public class Visit {

    @Size(max = 255)
    private String description;

    @NotNull
    @Past
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date visitDate;

    @RelatedTo(type = "PATIENT", direction = Direction.OUTGOING, elementClass = Pet.class)
    @ManyToOne(targetEntity = Pet.class)
    private Set<com.springone.petclinic.domain.Pet> pet;

    @RelatedTo(type = "DOCTOR", direction = Direction.OUTGOING, elementClass = Vet.class)
    @ManyToOne(targetEntity = Vet.class)
    private Set<com.springone.petclinic.domain.Vet> vet;
}
