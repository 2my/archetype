package no.antares.kickstart.item;

import javax.annotation.Resource;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

@Named("itemBean")
@Scope("session")
public class ItemBean {
	private ItemService	itemService	= null;
	private Integer			selectedId	= null;
	private String			name			= null;

	@Resource(name = "itemService")
	protected void setItemService(ItemService serviceIn) {
		itemService = serviceIn;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Item getSelectedItem() {
		if ( selectedId == null )
			return null;
		return itemService.findById( selectedId );
	}
	public Integer getSelectedId() {
		return selectedId;
	}
	public void setSelectedId(Integer id) {
		selectedId = id;
	}

	public String send() {
		return ( "send success" );
	}

	public String save() {
		return ( "save success" );
	}

}
