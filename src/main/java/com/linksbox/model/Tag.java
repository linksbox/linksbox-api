package com.linksbox.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author habib
 *
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "TAG")
public class Tag extends AbstractBaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "SQ_TAG_ID")
	@SequenceGenerator(name  = "SQ_TAG_ID", sequenceName = "SQ_TAG_ID", allocationSize = 1)
	private Long id;

	@NotNull
	@Column(name = "UUID", updatable = false)
	private UUID uuid;

	@Column(name = "NAME")
	private String name;

	@ManyToMany(mappedBy = "tags")
	private List<Link> links = new ArrayList<>();
	
	@PrePersist
	public void onPreInsert() {
		setUuid(UUID.randomUUID());
	}
}
