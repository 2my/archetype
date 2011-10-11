package no.antares.kickstart.item;


public interface ItemRepository {

    public Item findById(Integer id);

    public void update(Item i);

}
