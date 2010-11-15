package honeycrm.client.actions;

import honeycrm.client.admin.LogConsole;
import honeycrm.client.dto.Dto;

public class CreateContractAction extends AbstractAction {
	private static final long serialVersionUID = -1517848188241541631L;

	public CreateContractAction() {
	}
	
	@Override
	public void doAction(final Dto offering) {
		if (!"Offering".equals(offering.getModule())) {
			LogConsole.log("This is no offering dto object: '" + offering.getModule() + "'");
			return;
		}
		
		final Dto contract = offering.copy();
		contract.setModule("Contract");
		
		linkOfferingToContract(offering, contract);
	}

	private void linkOfferingToContract(final Dto offering, final Dto contract) {
		contract.set("offeringID", offering.getId());
		
		/*ServiceRegistry.createService().create(contract, new AsyncCallback<Long>() {
			@Override
			public void onSuccess(final Long contractID) {
				linkContractToOffering(offering, contractID);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Could not create contract");
			}
		});*/
	}

	private void linkContractToOffering(final Dto offering, final Long contractID) {
		offering.set("contractID", contractID);
		
		/*ServiceRegistry.updateService().update(offering, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				// forward the user to the created contract.
				// TODO re-implement
				// TabCenterView.instance().openEditView("Contract", contractID);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Could not update offering");
			}
		});*/
	}
}
