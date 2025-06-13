package org.siemac.metamac.srm.web.concept.widgets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderClosedEvent;
import com.smartgwt.client.widgets.tree.events.FolderClosedHandler;
import com.smartgwt.client.widgets.tree.events.FolderOpenedEvent;
import com.smartgwt.client.widgets.tree.events.FolderOpenedHandler;

public class ConceptsTreeGrid extends BaseConceptsTreeGrid {

    private NewConceptWindow         newConceptWindow;
    private DeleteConfirmationWindow deleteConfirmationWindow;

    private MenuItem                 createConceptMenuItem;
    private MenuItem                 deleteConceptMenuItem;

    private ItemVisualisationResult  selectedConcept;

    // Internal representation that helps us to recover opened nodes after reloading tree
    private Map<String, String>      treeOpenStates = new HashMap<String, String>();

    public ConceptsTreeGrid() {
        super(true, false);
        init(true);
    }

    public void init(boolean canCollapseGroup) {

        getField(ItemDS.CODE).setWidth("45%");

        setCanReorderRecords(true);
        setCanAcceptDroppedRecords(true);
        setCanDragRecordsOut(false);
        setDragDataAction(DragDataAction.MOVE);
        setShowOpenIcons(true);
        setShowDropIcons(true);

        // Context menu

        createConceptMenuItem = new MenuItem(MetamacSrmWeb.getConstants().conceptCreate());
        createConceptMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                newConceptWindow = new NewConceptWindow(MetamacSrmWeb.getConstants().conceptCreate(), conceptSchemeMetamacDto.getType());
                newConceptWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newConceptWindow.validateForm()) {
                            ConceptMetamacDto conceptMetamacDto = newConceptWindow.getNewConceptDto();
                            conceptMetamacDto.setItemSchemeVersionUrn(conceptSchemeMetamacDto.getUrn()); // Set concept scheme URN
                            conceptMetamacDto.setItemParentUrn(selectedConcept != null ? selectedConcept.getUrn() : null); // Set concept parent URN
                            getBaseConceptUiHandlers().saveConcept(conceptMetamacDto);
                            newConceptWindow.destroy();
                        }
                    }
                });
            }
        });

        deleteConceptMenuItem = new MenuItem(MetamacSrmWeb.getConstants().conceptDelete());
        deleteConfirmationWindow = new DeleteConfirmationWindow(MetamacSrmWeb.getConstants().conceptDeleteTitle(), MetamacSrmWeb.getConstants().conceptDeleteConfirmation());
        deleteConfirmationWindow.getYesButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getBaseConceptUiHandlers().deleteConcept(selectedConcept);
            }
        });
        deleteConceptMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        addItemsToContextMenu(createConceptMenuItem, deleteConceptMenuItem);
        // Tree open state

        addFolderOpenedHandler(new FolderOpenedHandler() {

            @Override
            public void onFolderOpened(FolderOpenedEvent event) {
                saveTreeOpenState();
            }
        });

        addFolderClosedHandler(new FolderClosedHandler() {

            @Override
            public void onFolderClosed(FolderClosedEvent event) {
                saveTreeOpenState();
            }
        });
    }

    public void setConcepts(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ConceptMetamacVisualisationResult> concepts) {
        this.conceptSchemeMetamacDto = conceptSchemeMetamacDto;
        super.setItems(conceptSchemeMetamacDto, concepts);
    }

    @Override
    public void setItems(ItemSchemeDto itemSchemeDto, List<ConceptMetamacVisualisationResult> itemMetamacResults) {
        super.setItems(itemSchemeDto, itemMetamacResults);
        recoverOpenState();
    }

    private boolean canCreateConcept() {
        return ConceptsClientSecurityUtils.canCreateConcept(conceptSchemeMetamacDto);
    }

    private boolean canDeleteConcept(String nodeName) {
        return !SCHEME_NODE_NAME.equals(nodeName) && ConceptsClientSecurityUtils.canDeleteConcept(conceptSchemeMetamacDto);
    }

    @Override
    protected com.smartgwt.client.widgets.viewer.DetailViewerField[] getDetailViewerFields() {
        return ResourceFieldUtils.getConceptDetailViewerFields();
    }

    @Override
    protected void onNodeContextClick(String nodeName, ConceptMetamacVisualisationResult itemMetamacResult) {
        selectedConcept = itemMetamacResult;
        createConceptMenuItem.setEnabled(canCreateConcept());
        deleteConceptMenuItem.setEnabled(canDeleteConcept(nodeName));
        showContextMenu();

    }

    private void recoverOpenState() {
        if (treeOpenStates.containsKey(itemSchemeDto.getUrn()) && !StringUtils.isBlank(treeOpenStates.get(itemSchemeDto.getUrn()))) {
            setOpenState(treeOpenStates.get(itemSchemeDto.getUrn()));
        }
    }

    private void saveTreeOpenState() {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            @Override
            public void execute() {
                treeOpenStates.put(itemSchemeDto.getUrn(), getOpenState());
            }
        });
    }

    @Override
    public void removeHandlerRegistrations() {
        super.removeHandlerRegistrations();
        folderDropHandlerRegistration.removeHandler();
    }
}
