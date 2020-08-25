package com.linksbox.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
@Table(name = "LINK", uniqueConstraints={@UniqueConstraint(columnNames={"url"})})
public class Link extends AbstractBaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "SQ_LINK_ID")
	@SequenceGenerator(name = "SQ_LINK_ID", sequenceName = "SQ_LINK_ID", allocationSize = 1)
	private Long id;

	@NotNull
	@Column(name = "UUID", updatable = false)
	private UUID uuid;

	@Column(name = "TITLE")
	private String title;
	
	@Column(name = "URL")
	private String url;
	
	@Column(name = "NOTES")
	private String notes;
	
	@ManyToMany
	@JoinTable(
	  name = "LINK_TAG",
	  joinColumns = @JoinColumn(name = "LINK_ID"),
	  inverseJoinColumns = @JoinColumn(name = "TAG_ID"))
	private List<Tag> tags = new ArrayList<>();
	
	@PrePersist
	public void onPreInsert() {
		setUuid(UUID.randomUUID());
	}
}
