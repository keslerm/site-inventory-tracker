package com.dasbiersec.sit.spring.repos;

import com.dasbiersec.sit.spring.model.Alert;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends CrudRepository<Alert, Long>
{
}
