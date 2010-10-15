package com.springone.petclinic.domain;

import org.springframework.datastore.graph.api.GraphEntity;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.entity.RooEntity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.springone.petclinic.reference.PetType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import java.util.Set;
import org.springframework.datastore.graph.api.GraphEntityRelationship;
import org.springframework.datastore.graph.api.Direction;
import com.springone.petclinic.domain.Owner;
import javax.persistence.ManyToOne;

@GraphEntity
@RooToString
@RooJavaBean
@RooEntity
public class Pet {

    @NotNull
    @Size(min = 1)
    private String name;

    @NotNull
    @Enumerated
    private PetType type;

    @NotNull
    @Min(0L)
    private Float weight;

    @NotNull
    private boolean sendReminders;

    @GraphEntityRelationship(type = "OWNS", direction = Direction.INCOMING, elementClass = Owner.class)
    @ManyToOne(targetEntity = Owner.class)
    private Set<com.springone.petclinic.domain.Owner> owner;
}
