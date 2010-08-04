package honeycrm.client.actions;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import honeycrm.client.ServiceRegistry;
import honeycrm.client.TabCenterView;
import honeycrm.client.admin.LogConsole;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.DtoModuleRegistry;
import honeycrm.client.dto.ModuleDto;

public class CreateContractAction extends AbstractAction {
	private static final long serialVersionUID = -1517848188241541631L;

	public CreateContractAction() {
	}
	
	@Override
	public void doAction(final Dto offering) {
		if (!"offering".equals(offering.getModule())) {
			LogConsole.log("This is no offering dto object: '" + offering.getModule() + "'");
			return;
		}
		
		final ModuleDto contractModule = DtoModuleRegistry.instance().get("contract");
		final Dto contract = contractModule.createDto();

		for (final String key: offering.getAllData().keySet()) {
			contract.set(key, offering.get(key));
		}
		
		linkOfferingToContract(offering, contract);
	}

	private void linkOfferingToContract(final Dto offering, final Dto contract) {
		contract.set("offeringID", offering.getId());
		
		ServiceRegistry.commonService().create(contract, new AsyncCallback<Long>() {
			@Override
			public void onSuccess(final Long contractID) {
				linkContractToOffering(offering, contractID);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Could not create contract");
			}
		});
	}

	private void linkContractToOffering(final Dto offering, final Long contractID) {
		offering.set("contractID", contractID);
		
		ServiceRegistry.commonService().update(offering, offering.getId(), new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				/**
				 * forward the user to the created contract.
				 */
				TabCenterView.instance().showModuleTabWithId("contract", contractID);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Could not update offering");
			}
		});
	}
}
