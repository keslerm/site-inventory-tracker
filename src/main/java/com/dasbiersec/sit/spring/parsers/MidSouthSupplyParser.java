package com.dasbiersec.sit.spring.parsers;

import com.dasbiersec.sit.spring.datasources.DataSource;
import com.dasbiersec.sit.spring.model.InventoryItem;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MidSouthSupplyParser implements Parser
{
    private Logger log = Logger.getLogger(getClass());

    private DataSource dataSource;
    private String identifier;

    private int totalPages = 1;

    @Override
    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @Override
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    @Override
    public ArrayList<InventoryItem> process()
    {
        ArrayList<InventoryItem> inventoryItems = new ArrayList<InventoryItem>();

        // get total pages first
        Document doc = dataSource.getDocument();

        // get total pages
        totalPages = getTotalPages(doc);

        // begin looping through pages
        for (int i = 0; i < totalPages; i++)
        {
            log.info("Processing page " + dataSource.currentPage());

            // check parse
            Elements elements = doc.select("#Item_holder");

            for (Element element : elements)
            {
                Elements itemElements = element.select("li");

                InventoryItem item = new InventoryItem();

                item.setName(cleanName(itemElements.get(1).text()));
                item.setPrice(itemElements.get(5).text().replace("Price: $", "").trim());
                item.setStatus(itemElements.get(4).text().replace("Status:", ""));
                item.setUrl("http://www.midsouthshooterssupply.com/" + itemElements.get(0).select("a").get(0).attr("href"));
                item.setIdentifier(identifier);

                if (itemElements.get(4).text().contains("Out of Stock"))
                    item.setInStock(false);
                else
                    item.setInStock(true);

                inventoryItems.add(item);
            }

            dataSource.nextPage();
            doc = dataSource.getDocument();
        }

        return inventoryItems;

    }

    protected int getTotalPages(Document doc)
    {
        int totalPages = 1;

        Pattern pat = Pattern.compile("Item Total \\(([0-9]+)\\)");
        Elements elements = doc.select(".Items_Sort_M .basic_rt_M");

        if (elements != null)
        {
            Element element = elements.get(0);

            String total = element.text();

            Matcher matcher = pat.matcher(total);

            if (matcher.find())
            {
                log.info("Found " + matcher.group(1) + " items");
                int totalItems = Integer.parseInt(matcher.group(1));

                totalPages = (int) Math.ceil(totalItems / 20.0);
            }
        }
        else
        {
            log.info("Found no matching item nav elements");
        }

        return totalPages;
    }

    protected String cleanName(String original)
    {
        String newName = original.replace("\t", "");
        newName = newName.replace("&nbsp;", " ");

        // fix no break space character
        newName = newName.replace(" ", " ");

        return newName;
    }
}
