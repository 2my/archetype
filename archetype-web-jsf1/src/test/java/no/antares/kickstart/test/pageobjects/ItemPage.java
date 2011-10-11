package no.antares.kickstart.test.pageobjects;

import no.antares.kickstart.test.util.SeleniumStarter;

import org.openqa.selenium.By;


/** 
 * @author Tommy Skodje
 */
public class ItemPage extends PageObject {
    public ItemPage(SeleniumStarter utils) {
        super(utils, "item.jsf");
        throwIfTitleNot("Archetype web demo");
    }

    public void loadItem(int idS) {
    	String id	= Integer.toString( idS );
    	clearAndType(By.id("loadItemForm:selectItemId"), id);
    	findElement(By.id("loadItemForm:loadItem")).click();
    }

    public String itemName() {
        return findElement(By.id("itemName")).getText();
    }

}
