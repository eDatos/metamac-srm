package org.siemac.metamac.srm.core.category.serviceimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacRepository;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.serviceimpl.CategoriesCopyCallbackImpl;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

/**
 * Override callback copying Metamac metadata
 */
// @Component() defined in spring xml configuration to set class attributes
public class CategoriesCopyCallbackMetamacImpl extends CategoriesCopyCallbackImpl {

    @Autowired
    private CategoriesMetamacService  categoriesMetamacService;

    @Autowired
    private CategoryMetamacRepository categoryMetamacRepository;

    @Override
    public ItemSchemeVersion createItemSchemeVersion() {
        return new CategorySchemeVersionMetamac();
    }

    @Override
    public void copyItemSchemeVersion(ItemSchemeVersion sourceItemSchemeVersion, ItemSchemeVersion targetItemSchemeVersion) {
        super.copyItemSchemeVersion(sourceItemSchemeVersion, targetItemSchemeVersion);
        CategorySchemeVersionMetamac target = (CategorySchemeVersionMetamac) targetItemSchemeVersion;
        target.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        target.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
        target.setStreamMessageStatus(StreamMessageStatusEnum.PENDING);
    }

    @Override
    public Item createItem() {
        return new CategoryMetamac();
    }

    @Override
    public void copyItem(Item sourceItem, ItemResult sourceItemResult, Item targetItem, boolean isDefaultMaintainer) {
        super.copyItem(sourceItem, sourceItemResult, targetItem, isDefaultMaintainer);

        copyCategory(sourceItem, targetItem);
    }

    @Override
    public void copyItemWithoutUnmodifiedMetadata(Item sourceItem, ItemResult sourceItemResult, Item targetItem, boolean isDefaultMaintainer) {
        super.copyItemWithoutUnmodifiedMetadata(sourceItem, sourceItemResult, targetItem, isDefaultMaintainer);

        copyCategory(sourceItem, targetItem);

        // IMPORTANT! If any InternationalString is added, do an efficient query and retrieve from sourceItemResult
    }

    private void copyCategory(Item sourceItem, Item targetItem) {
        CategoryMetamac source = (CategoryMetamac) sourceItem;
        CategoryMetamac target = (CategoryMetamac) targetItem;
        target.setCategoryElement(source.getCategoryElement());
        copyEniSchemas(source, target);
        copyCategorySchemeOrderVisualisations(source, target);

        // IMPORTANT! If any InternationalString is added, do an efficient query and retrieve from sourceItemResult
    }

    private void copyCategorySchemeOrderVisualisations(CategoryMetamac source, CategoryMetamac target) {
        for (int i = 1; i <= SrmConstants.CATEGORY_SCHEME_ORDER_VISUALISATION_MAXIMUM_NUMBER; i++) {
            Integer order = SrmServiceUtils.getCategorySchemeOrder(source, i);
            SrmServiceUtils.setCategorySchemeOrder(target, i, order);
        }
    }

    private void copyEniSchemas(CategoryMetamac source, CategoryMetamac target) {

        if (source.getEniSchemas() == null || source.getEniSchemas().isEmpty()) {
            return;
        }

        for (EniSchemaCatalog eniSchema : source.getEniSchemas()) {
            target.addEniSchema(eniSchema);
        }
    }

    @Override
    public void createItemSchemeVersionRelations(ServiceContext ctx, ItemSchemeVersion sourceItemSchemeVersion, ItemSchemeVersion targetItemSchemeVersion) throws MetamacException {
        super.createItemSchemeVersionRelations(ctx, sourceItemSchemeVersion, targetItemSchemeVersion);
        CategorySchemeVersionMetamac source = (CategorySchemeVersionMetamac) sourceItemSchemeVersion;
        CategorySchemeVersionMetamac target = (CategorySchemeVersionMetamac) targetItemSchemeVersion;

        categoriesMetamacService.versioningCategorySchemeOrderVisualisations(ctx, source, target);
    }

    @Override
    public List<ItemResult> findItemsEfficiently(Long itemSchemeId) throws MetamacException {
        return categoryMetamacRepository.findCategoriesByCategorySchemeUnordered(itemSchemeId, SrmServiceUtils.getItemResultSelection(getCopyOperationType()));
    }
}