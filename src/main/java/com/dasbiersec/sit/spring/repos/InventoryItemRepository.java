package com.dasbiersec.sit.spring.repos;

import com.dasbiersec.sit.spring.model.InventoryItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends CrudRepository<InventoryItem, Long>
{
    public InventoryItem getInventoryItemByUrl(String url);
    public Iterable<InventoryItem> getInventoryItemByIdentifier(String identifier);
    public Iterable<InventoryItem> findItemsByName(String name);
}
