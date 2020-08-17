package com.linksbox.model;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

/**
 * Abstract base class for all entity classes - technical and auditing
 * attributes
 */
@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AbstractBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@CreatedBy
	@Column(name = "CREATOR", updatable = false)
	private String creator;

	@NotNull
	@CreatedDate
	@Column(name = "CREATED", updatable = false)
	private OffsetDateTime created;

	@NotNull
	@LastModifiedBy
	@Column(name = "MODIFIER")
	private String modifier;

	@NotNull
	@LastModifiedDate
	@Column(name = "MODIFIED")
	private OffsetDateTime modified;

	@Version
	@Column(name = "VERSION")
	private Integer version;

}
