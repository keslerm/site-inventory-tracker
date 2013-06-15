package com.dasbiersec.sit.spring.repos;

import com.dasbiersec.sit.spring.model.Scraper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScraperRepository extends CrudRepository<Scraper, Long>
{
}
