package org.siemac.metamac.srm.rest.internal.v1_0.mapper.category;

import java.util.List;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.api.export.mapper.PlainTextResource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryElementResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryElements;
import org.siemac.metamac.srm.core.category.domain.CategoryElement;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;

import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public interface CategoriesDo2RestMapperV10 {

    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategorySchemes toCategorySchemes(PagedResult<CategorySchemeVersionMetamac> sources, String agencyID, String resourceID,
            String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryScheme toCategoryScheme(CategorySchemeVersionMetamac source);

    public Categories toCategories(PagedResult<CategoryMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit,
            Set<String> fields);
    List<PlainTextResource> toPlainTextCategories(Categories source, List<String> languages, Set<String> fields, String format) throws MetamacException;

    public Categories toCategories(List<ItemResult> sources, CategorySchemeVersionMetamac categorySchemeVersion, Set<String> fields);
    public Category toCategory(CategoryMetamac source);

    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisations toCategorisations(PagedResult<Categorisation> sources, String agencyID, String resourceID, String query,
            String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisation toCategorisation(Categorisation source);

    public ResourceLink toCategorySchemeSelfLink(String agencyID, String resourceID, String version);
    public String toCategorySchemeManagementApplicationLink(String categorySchemeUrn);

    public CategoryElements toCategoryElements(PagedResult<CategoryElement> sourcesPagedResult, String query, String orderBy, Integer limit);
    public CategoryElementResourceInternal toCategoryElementResourceInternal(CategoryElement source);
}