package org.siemac.metamac.srm.rest.external.v1_0.mapper.category;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.api.export.mapper.PlainTextResource;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.Item;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Categorisations;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategoryElementResource;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategoryElements;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategoryResource;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategoryScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategorySchemes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.EniSchemaTypes;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.srm.core.category.domain.CategoryElement;
import org.siemac.metamac.srm.core.category.domain.CategoryElementResult;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.external.service.HtmlLinkUtil;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.base.ItemSchemeBaseDo2RestMapperV10Impl;
import org.siemac.metamac.srm.rest.external.v1_0.service.utils.SrmRestInternalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import org.siemac.metamac.srm.core.code.enume.domain.RelatedResourceSrmTypeEnum;

@Component
public class CategoriesDo2RestMapperV10Impl extends ItemSchemeBaseDo2RestMapperV10Impl implements CategoriesDo2RestMapperV10 {

    @Override
    public CategorySchemes toCategorySchemes(PagedResult<CategorySchemeVersionMetamac> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy, Integer limit) throws MetamacException{

        CategorySchemes targets = new CategorySchemes();
        targets.setKind(SrmRestConstants.KIND_CATEGORY_SCHEMES);

        // Pagination
        String baseLink = toCategorySchemesLink(agencyID, resourceID, null);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (CategorySchemeVersionMetamac source : sourcesPagedResult.getValues()) {
            Resource target = toResource(source);
            targets.getCategorySchemes().add(target);
        }
        return targets;
    }

    @Override
    public CategoryScheme toCategoryScheme(CategorySchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }

        CategoryScheme target = new CategoryScheme();

        target.setKind(SrmRestConstants.KIND_CATEGORY_SCHEME);
        target.setSelfLink(toCategorySchemeSelfLink(source));
        target.setParentLink(toCategorySchemeParentLink(source));
        target.setChildLinks(toCategorySchemeChildLinks(source));

        toItemScheme(source, source.getLifeCycleMetadata(), target);
        target.setVisualizerHtmlLink(HtmlLinkUtil.getCodelistVisualizerHtmlLink(source.getMaintainableArtefact(), null, RelatedResourceSrmTypeEnum.CATEGORY_SCHEME));

        return target;
    }

    @Override
    public Categories toCategories(PagedResult<CategoryMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit,
            Set<String> fields) throws MetamacException {

        Categories targets = new Categories();
        targets.setKind(SrmRestConstants.KIND_CATEGORIES);

        // Pagination
        String baseLink = toCategoriesLink(agencyID, resourceID, version);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (CategoryMetamac source : sourcesPagedResult.getValues()) {
            CategoryResource target = toResource(source, fields);
            targets.getCategories().add(target);
        }
        return targets;
    }

    @Override
    public Categories toCategories(List<ItemResult> sources, CategorySchemeVersionMetamac categorySchemeVersion, Set<String> fields) throws MetamacException {

        Categories targets = new Categories();
        targets.setKind(SrmRestConstants.KIND_CATEGORIES);

        // No pagination
        targets.setSelfLink(toCategoriesLink(categorySchemeVersion));
        targets.setTotal(BigInteger.valueOf(sources.size()));

        // Values
        for (ItemResult source : sources) {
            CategoryResource target = toResource(source, categorySchemeVersion, fields);
            targets.getCategories().add(target);
        }
        return targets;
    }

    @Override
    public Category toCategory(CategoryMetamac source, Set<String> fields) {
        if (source == null) {
            return null;
        }
        Category target = new Category();

        target.setKind(SrmRestConstants.KIND_CATEGORY);
        target.setSelfLink(toCategorySelfLink(source));
        target.setParentLink(toCategoryParentLink(source));
        target.setChildLinks(toCategoryChildLinks(source));

        toItem(source, target);

        target.setNestedId(source.getNameableArtefact().getCodeFull());

        if (SrmRestInternalUtils.containsField(fields, SrmRestConstants.FIELD_INCLUDE_CATEGORY_ELEMENT)) {
            target.setCategoryElement(toCategoryElementResource(source.getCategoryElement()));
        }

        if (source.getEniSchemas() != null && !source.getEniSchemas().isEmpty()) {
            target.setEniSchemaTypes(toEniSchemaTypes(new ArrayList<EniSchemaCatalog>(source.getEniSchemas())));
        }
        target.setVisualizerHtmlLink(HtmlLinkUtil.getCodelistVisualizerHtmlLink(source.getItemSchemeVersion().getMaintainableArtefact(), source.getNameableArtefact().getCode(), RelatedResourceSrmTypeEnum.CATEGORY));
        return target;
    }

    @Override
    public List<PlainTextResource> toPlainTextCategories(Categories source, List<String> languages, Set<String> fields, String format) throws MetamacException {
        if (source == null) {
            return new ArrayList<>();
        }

        List<PlainTextResource> target = new ArrayList<>();

        for (CategoryResource category : source.getCategories()) {
            target.add(toFlattenCategory(category, languages, fields, format));
        }

        return target;
    }

    private PlainTextResource toFlattenCategory(CategoryResource source, List<String> languages, Set<String> fields, String format) {
        PlainTextResource target = new PlainTextResource();
        target.getFields().put(SrmConstants.TSV_HEADER_CODE, source.getId());
        target.getFields().put(SrmConstants.TSV_HEADER_PARENT, source.getParent());
        target.getFields().put(SrmConstants.TSV_HEADER_URN, source.getUrn());
        target.getFields().putAll(SrmRestInternalUtils.internationalString2MapExport(SrmConstants.TSV_HEADER_NAME, source.getName(), languages, format));

        if (fields.contains(SrmRestConstants.FIELD_INCLUDE_DESCRIPTION)) {
            target.getFields().putAll(SrmRestInternalUtils.internationalString2MapExport(SrmConstants.TSV_HEADER_DESCRIPTION, source.getDescription(), languages, format));
        }

        if (fields.contains(SrmRestConstants.FIELD_INCLUDE_ENI_SCHEME)) {
            SrmRestInternalUtils.toFlattenEniSchemas(source.getEniSchemaTypes().getEniSchemaTypes(), target, format);
        }

        return target;
    }

    private EniSchemaTypes toEniSchemaTypes(List<EniSchemaCatalog> source) {
        if (source == null) {
            return null;
        }
        EniSchemaTypes target = new EniSchemaTypes();
        target.setTotal(BigInteger.valueOf(source.size()));
        for (EniSchemaCatalog eniSchema : source) {
            target.getEniSchemaTypes().add(toItem(eniSchema));
        }

        return target;
    }

    private Item toItem(EniSchemaCatalog source) {
        if (source == null) {
            return null;
        }
        Item target = new Item();
        target.setId(source.getIdentifier());
        target.setName(toInternationalString(source.getDescription()));
        return target;
    }

    @Override
    public Categorisations toCategorisations(PagedResult<Categorisation> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy, Integer limit) {

        Categorisations targets = new Categorisations();
        targets.setKind(SrmRestConstants.KIND_CATEGORISATIONS);

        // Pagination
        String baseLink = toCategorisationsLink(agencyID, resourceID, null);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (Categorisation source : sourcesPagedResult.getValues()) {
            Resource target = toResource(source);
            targets.getCategorisations().add(target);
        }
        return targets;
    }

    @Override
    public org.siemac.metamac.rest.structural_resources.v1_0.domain.Categorisation toCategorisation(Categorisation source) {
        if (source == null) {
            return null;
        }

        org.siemac.metamac.rest.structural_resources.v1_0.domain.Categorisation target = new org.siemac.metamac.rest.structural_resources.v1_0.domain.Categorisation();

        target.setKind(SrmRestConstants.KIND_CATEGORISATION);
        target.setSelfLink(toCategorisationSelfLink(source));
        target.setParentLink(toCategorisationParentLink(source));
        target.setChildLinks(toCategorisationChildLinks(source));

        toMaintainableArtefact(source.getMaintainableArtefact(), null, target);

        target.setSource(source.getArtefactCategorised().getUrn());
        target.setTarget(source.getCategory().getNameableArtefact().getUrn());

        return target;
    }

    @Override
    public CategoryElements toCategoryElements(PagedResult<CategoryElement> sourcesPagedResult, String query, String orderBy, Integer limit) {

        CategoryElements targets = new CategoryElements();
        targets.setKind(SrmRestConstants.KIND_CATEGORY_ELEMENTS);

        // Pagination
        String baseLink = toCategoryElementsLink();
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (CategoryElement source : sourcesPagedResult.getValues()) {
            CategoryElementResource target = toCategoryElementResource(source);
            targets.getCategoryElements().add(target);
        }
        return targets;
    }

    @Override
    protected boolean canItemSchemeVersionBeProvidedByApi(ItemSchemeVersion source) {
        return true; // no additional conditions
    }

    private ResourceLink toCategorySchemeSelfLink(CategorySchemeVersionMetamac source) {
        return toResourceLink(SrmRestConstants.KIND_CATEGORY_SCHEME, toCategorySchemeLink(source));
    }

    private ResourceLink toCategorySchemeParentLink(CategorySchemeVersionMetamac source) {
        return toResourceLink(SrmRestConstants.KIND_CATEGORY_SCHEMES, toCategorySchemesLink(null, null, null));
    }

    private ChildLinks toCategorySchemeChildLinks(CategorySchemeVersionMetamac source) {
        ChildLinks targets = new ChildLinks();
        targets.getChildLinks().add(toResourceLink(SrmRestConstants.KIND_CATEGORIES, toCategoriesLink(source)));
        targets.setTotal(BigInteger.valueOf(targets.getChildLinks().size()));
        return targets;
    }

    private ResourceLink toCategorySelfLink(com.arte.statistic.sdmx.srm.core.category.domain.Category source) {
        return toResourceLink(SrmRestConstants.KIND_CATEGORY, toCategoryLink(source));
    }
    private ResourceLink toCategorySelfLink(ItemResult source, CategorySchemeVersion categorySchemeVersion) {
        return toResourceLink(SrmRestConstants.KIND_CATEGORY, toCategoryLink(source, categorySchemeVersion));
    }

    private ResourceLink toCategoryParentLink(com.arte.statistic.sdmx.srm.core.category.domain.Category source) {
        return toResourceLink(SrmRestConstants.KIND_CATEGORIES, toCategoriesLink(source.getItemSchemeVersion()));
    }

    private ChildLinks toCategoryChildLinks(CategoryMetamac source) {
        // nothing
        return null;
    }

    private ResourceLink toCategorisationSelfLink(Categorisation source) {
        return toResourceLink(SrmRestConstants.KIND_CATEGORISATION, toCategorisationLink(source));
    }
    private ResourceLink toCategorisationParentLink(Categorisation source) {
        return toResourceLink(SrmRestConstants.KIND_CATEGORISATIONS, toCategorisationsLink(null, null, null));
    }

    private ChildLinks toCategorisationChildLinks(Categorisation source) {
        // nothing
        return null;
    }

    private Resource toResource(CategorySchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        toResource(source.getMaintainableArtefact(), SrmRestConstants.KIND_CATEGORY_SCHEME, toCategorySchemeSelfLink(source), target);
        target.setVisualizerHtmlLink(HtmlLinkUtil.getCodelistVisualizerHtmlLink(source.getMaintainableArtefact(), null, RelatedResourceSrmTypeEnum.CATEGORY_SCHEME));
        return target;
    }

    private CategoryResource toResource(CategoryMetamac source, Set<String> fields) {
        if (source == null) {
            return null;
        }
        CategoryResource target = new CategoryResource();
        toResource(source, SrmRestConstants.KIND_CATEGORY, toCategorySelfLink(source), target, fields);
        target.setNestedId(source.getNameableArtefact().getCodeFull());

        setVisibleCategoryFields(source, target, fields);
        target.setVisualizerHtmlLink(HtmlLinkUtil.getCodelistVisualizerHtmlLink(source.getItemSchemeVersion().getMaintainableArtefact(), source.getNameableArtefact().getCode(), RelatedResourceSrmTypeEnum.CATEGORY));
        return target;
    }

    private CategoryResource toResource(ItemResult source, CategorySchemeVersionMetamac categorySchemeVersion, Set<String> fields) {
        if (source == null) {
            return null;
        }
        CategoryResource target = new CategoryResource();
        toResource(source, SrmRestConstants.KIND_CATEGORY, toCategorySelfLink(source, categorySchemeVersion), target, categorySchemeVersion.getMaintainableArtefact().getIsImported(), fields);
        target.setNestedId(source.getCodeFull());

        setVisibleCategoryFields(source, target, fields);
        target.setVisualizerHtmlLink(HtmlLinkUtil.getCodelistVisualizerHtmlLink(categorySchemeVersion.getMaintainableArtefact(), source.getCode(), RelatedResourceSrmTypeEnum.CATEGORY));
        return target;
    }

    private Resource toResource(Categorisation source) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        toResource(source.getMaintainableArtefact(), SrmRestConstants.KIND_CATEGORISATION, toCategorisationSelfLink(source), target);
        return target;
    }

    private String toCategorySchemesLink(String agencyID, String resourceID, String version) {
        return toMaintainableArtefactLink(toSubpathItemSchemes(), agencyID, resourceID, version);
    }
    private String toCategorySchemeLink(ItemSchemeVersion itemSchemeVersion) {
        return toItemSchemeLink(toSubpathItemSchemes(), itemSchemeVersion);
    }
    private String toCategoriesLink(String agencyID, String resourceID, String version) {
        return toItemsLink(toSubpathItemSchemes(), toSubpathItems(), agencyID, resourceID, version);
    }
    private String toCategoriesLink(ItemSchemeVersion itemSchemeVersion) {
        return toItemsLink(toSubpathItemSchemes(), toSubpathItems(), itemSchemeVersion);
    }
    private String toCategoryLink(com.arte.statistic.sdmx.srm.core.base.domain.Item item) {
        return toItemLink(toSubpathItemSchemes(), toSubpathItems(), item);
    }
    private String toCategoryLink(ItemResult item, ItemSchemeVersion itemSchemeVersion) {
        return toItemLink(toSubpathItemSchemes(), toSubpathItems(), item, itemSchemeVersion);
    }

    private String toCategorisationsLink(String agencyID, String resourceID, String version) {
        return toMaintainableArtefactLink(toSubpathCategorisations(), agencyID, resourceID, version);
    }
    private String toCategorisationLink(Categorisation categorisation) {
        return toMaintainableArtefactLink(toSubpathCategorisations(), categorisation.getMaintainableArtefact());
    }

    private String toSubpathItemSchemes() {
        return SrmRestConstants.LINK_SUBPATH_CATEGORY_SCHEMES;
    }
    private String toSubpathItems() {
        return SrmRestConstants.LINK_SUBPATH_CATEGORIES;
    }
    private String toSubpathCategorisations() {
        return SrmRestConstants.LINK_SUBPATH_CATEGORISATIONS;
    }

    private void setVisibleCategoryFields(CategoryMetamac source, CategoryResource target, Set<String> fields) {
        if (SrmRestInternalUtils.containsField(fields, SrmRestConstants.FIELD_INCLUDE_ENI_SCHEME)) {
            target.setEniSchemaTypes(toEniSchemaTypes(new ArrayList<EniSchemaCatalog>(source.getEniSchemas())));
        }

        if (SrmRestInternalUtils.containsField(fields, SrmRestConstants.FIELD_INCLUDE_CATEGORY_ELEMENT)) {
            target.setCategoryElement(toCategoryElementResource(source.getCategoryElement()));
        }

    }

    private void setVisibleCategoryFields(ItemResult source, CategoryResource target, Set<String> fields) {
        if (SrmRestInternalUtils.containsField(fields, SrmRestConstants.FIELD_INCLUDE_ENI_SCHEME) && source.getExtensionPoint() != null) {
            target.setEniSchemaTypes(toEniSchemaTypes(((CategoryMetamacResultExtensionPoint) source.getExtensionPoint()).getEniSchemas()));
        }

        if (SrmRestInternalUtils.containsField(fields, SrmRestConstants.FIELD_INCLUDE_CATEGORY_ELEMENT) && source.getExtensionPoint() != null) {
            target.setCategoryElement(toResource(((CategoryMetamacResultExtensionPoint) source.getExtensionPoint()).getCategoryElement()));
        }
    }

    private CategoryElementResource toResource(CategoryElementResult source) {

        if (source == null) {
            return null;
        }

        CategoryElementResource target = new CategoryElementResource();

        toResource(source, SrmRestConstants.KIND_CATEGORY_ELEMENT, toCategoryElementSelfLink(source.getCode()), target, false);
        target.setName(toInternationalString(source.getShortName()));
        target.setVisible(source.getIsVisible());
        target.setLogoLink(toCategoryElementLogoLink(source.getCode()));

        return target;
    }

    private ResourceLink toCategoryElementLogoLink(String codeCategoryElement) {
        ResourceLink target = new ResourceLink();
        target.setKind(SrmRestConstants.KIND_CATEGORY_ELEMENT_LOGO);
        target.setHref(RestUtils.createLink(toCategoryElementLink(codeCategoryElement), SrmRestConstants.LINK_SUBPATH_CATEGORY_ELEMENT_LOGO));

        return target;
    }

    private Resource toResource(CategoryElement source) {
        if (source == null) {
            return null;
        }

        Resource target = new Resource();
        toResource(source.getIdentifiableArtefact(), SrmRestConstants.KIND_CATEGORY_ELEMENT, toCategoryElementSelfLink(source.getIdentifiableArtefact().getCode()), target, false);
        target.setName(toInternationalString(source.getShortName()));

        return target;
    }

    public CategoryElementResource toCategoryElementResource(CategoryElement source) {
        if (source == null) {
            return null;
        }

        CategoryElementResource target = new CategoryElementResource();
        toResource(source.getIdentifiableArtefact(), SrmRestConstants.KIND_CATEGORY_ELEMENT, toCategoryElementSelfLink(source.getIdentifiableArtefact().getCode()), target, false);
        target.setName(toInternationalString(source.getShortName()));
        target.setValidTo(toDate(source.getValidTo()));
        target.setValidFrom(toDate(source.getValidFrom()));
        target.setVisible(source.getIsVisible());
        target.setLogoLink(toCategoryElementLogoLink(source.getIdentifiableArtefact().getCode()));

        return target;
    }

    private ResourceLink toCategoryElementSelfLink(String codeCategoryElement) {
        ResourceLink target = new ResourceLink();
        target.setKind(SrmRestConstants.KIND_CATEGORY_ELEMENT);
        target.setHref(toCategoryElementLink(codeCategoryElement));
        return target;
    }

    private String toCategoryElementLink(String codeCategoryElement) {
        String link = RestUtils.createLink(getSrmApiExternalEndpointV10(), SrmRestConstants.LINK_SUBPATH_CATEGORY_ELEMENTS);
        return RestUtils.createLink(link, codeCategoryElement);
    }

    // API/categoryElements
    private String toCategoryElementsLink() {
        return RestUtils.createLink(getSrmApiExternalEndpointV10(), SrmRestConstants.LINK_SUBPATH_CATEGORY_ELEMENTS);
    }
}