package com.recargapay.app.domain.model;

import com.recargapay.app.domain.linsteners.UserRevisionListener;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Entity
@Table(name = "revinfo", schema = "sc_recargapay")
@RevisionEntity(UserRevisionListener.class)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserRevEntity extends DefaultRevisionEntity {

    private String username;
}
