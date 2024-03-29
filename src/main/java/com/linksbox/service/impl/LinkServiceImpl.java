package com.linksbox.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.linksbox.model.Link;
import com.linksbox.model.Tag;
import com.linksbox.persistence.LinkRepository;
import com.linksbox.service.LinkService;

@Service
public class LinkServiceImpl implements LinkService {

	@Autowired
	LinkRepository repo;

	@Override
	public Page<Link> search(String searchText, Pageable pageable) {
		return repo.findAll(SearchSpecificaton.findByFreeText(searchText), pageable);
	}

	@Override
	public Page<Link> getLinks(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	public Optional<Link> getLinkByUuid(UUID uuid) {
		return repo.findByUuid(uuid);
	}

	@Override
	public Link createOrUpdateLink(Link link) {
		return repo.save(link);
	}

	@Override
	public void deleteLink(Link link) {
		repo.delete(link);
	}

	@Override
	public List<Link> getLinksByTags(List<Tag> tags) {
		return repo.findByTagsIn(tags);
	}

	@Override
	public Optional<Link> getLinkByUrl(String url) {
		return repo.findByUrl(url);
	}

	private static class SearchSpecificaton {
		private static Specification<Link> findByFreeText(String searchText) {
			return new Specification<Link>() {
				private static final long serialVersionUID = 1L;

				@Override
				public Predicate toPredicate(Root<Link> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
					List<Predicate> predicates = new ArrayList<>();
					if (StringUtils.isNotBlank(searchText)) {
						String text = "%" + searchText.toLowerCase() + "%";
						Predicate searchTextPredicate = criteriaBuilder.or(
								criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), text),
								criteriaBuilder.like(criteriaBuilder.lower(root.get("url")), text),
								criteriaBuilder.like(criteriaBuilder.lower(root.get("notes")), text));
						predicates.add(criteriaBuilder.and(searchTextPredicate));
					}
					return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
				}

			};
		}
	}

}
