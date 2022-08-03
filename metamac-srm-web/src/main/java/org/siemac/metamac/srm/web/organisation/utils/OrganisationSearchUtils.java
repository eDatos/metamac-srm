package org.siemac.metamac.srm.web.organisation.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacBasicDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationWebCriteria;
import org.siemac.metamac.srm.web.shared.organisation.OrganisationSchemeWebCriteriaOrderEnum;
import org.siemac.metamac.srm.web.shared.organisation.OrganisationWebCriteriaOrderEnum;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class OrganisationSearchUtils {

    private OrganisationSearchUtils() {
        // without implement
    }

    // ------------------------------------------------------------------------------------------------------------
    // EXTERNAL RESOURCES ITEMS
    // ------------------------------------------------------------------------------------------------------------

    
    public static List<ExternalItemDto> organizationSchemeMetamacDtoToExternalItemDto(List<OrganisationSchemeMetamacBasicDto>  organizationsSchems) {
        List<ExternalItemDto> externalItems = new ArrayList<ExternalItemDto>(organizationsSchems.size());

        for (OrganisationSchemeMetamacBasicDto org : organizationsSchems) {
            ExternalItemDto dto = new ExternalItemDto();
            dto.setCode(org.getCode());
            dto.setTitle(org.getName());
            dto.setUrn(org.getUrn());
            dto.setUrnProvider(org.getUrnProvider());
            dto.setType(TypeExternalArtefactsEnum.valueOf(org.getType().getName()));
            dto.setCodeNested(org.getCode());
            externalItems.add(dto);

        }

        return externalItems;
    }

    public static List<ExternalItemDto> organizationMetamacDtoToExternalItemDto(List<OrganisationMetamacBasicDto> organizations) {
        List<ExternalItemDto> externalItems = new ArrayList<ExternalItemDto>(organizations.size());

        for (OrganisationMetamacBasicDto org : organizations) {
            ExternalItemDto dto = new ExternalItemDto();
            dto.setCode(org.getCode());
            dto.setTitle(org.getName());
            dto.setUrn(org.getUrn());
            dto.setUrnProvider(org.getUrnProvider());
            dto.setType(TypeExternalArtefactsEnum.valueOf(org.getType().getName()));
            externalItems.add(dto);

        }

        return externalItems;
    }

    // ORGANIZATION WEB CRITERIA

    // EXTERNAL ORGANISATION SCHEME CRITERIA TO ORGANISATION SCHEME WEB CRITERIA
    public static OrganisationSchemeWebCriteria getOrganisationSchemeWebCriteria(SrmExternalResourceRestCriteria itemSchemeRestCriteria, OrganisationSchemeTypeEnum[] types) {
        OrganisationSchemeWebCriteria organisationSchemeWebCriteria = new OrganisationSchemeWebCriteria();
        organisationSchemeWebCriteria.setOrderField(OrganisationSchemeWebCriteriaOrderEnum.CODE);
        organisationSchemeWebCriteria.setOrderType(OrderTypeEnum.ASC);
        organisationSchemeWebCriteria.setOnlyPublished(Boolean.TRUE);
        organisationSchemeWebCriteria.setOrganisationSchemeTypes(types);
        organisationSchemeWebCriteria.setCriteria(itemSchemeRestCriteria.getCriteria());
        organisationSchemeWebCriteria.setOnlyLastVersion(itemSchemeRestCriteria.isOnlyLastVersion());
        organisationSchemeWebCriteria.setUrn(itemSchemeRestCriteria.getUrn());
        organisationSchemeWebCriteria.setUrns(itemSchemeRestCriteria.getUrns());
        
        return organisationSchemeWebCriteria;
    }

    // EXTERNAL ORGANISATION CRITERIA TO ORGANISATION WEB CRITERIA
    public static OrganisationWebCriteria getOrganisationWebCriteria(SrmItemRestCriteria itemRestCriteria, OrganisationTypeEnum[] types) {
        OrganisationWebCriteria organisationWebCriteria = new OrganisationWebCriteria();
        organisationWebCriteria.setOrderField(OrganisationWebCriteriaOrderEnum.CODE);
        organisationWebCriteria.setOrderType(OrderTypeEnum.ASC);
        organisationWebCriteria.setOrganisationTypes(types);
        organisationWebCriteria.setCriteria(itemRestCriteria.getCriteria());
        organisationWebCriteria.setIsLastVersion(itemRestCriteria.isItemSchemeLastVersion());
        organisationWebCriteria.setUrn(itemRestCriteria.getUrn());
        organisationWebCriteria.setItemSchemeUrn(itemRestCriteria.getItemSchemeUrn());

        return organisationWebCriteria;
    }
    
    public static ExternalItemDto getExternalItemDto(RelatedResourceDto sourceProvider) {
        if (sourceProvider != null) {
            ExternalItemDto sourceProviderExternalItem = new ExternalItemDto();
            sourceProviderExternalItem.setCode(sourceProvider.getCode());
            sourceProviderExternalItem.setCodeNested(sourceProvider.getCode());
            sourceProviderExternalItem.setId(sourceProvider.getId());
            sourceProviderExternalItem.setTitle(sourceProvider.getTitle());
            sourceProviderExternalItem.setUrn(sourceProvider.getUrn());
            sourceProviderExternalItem.setUrnProvider(sourceProvider.getUrnProvider());
            return sourceProviderExternalItem;
        }
        return null;

    }

    public static RelatedResourceDto getRelatedResourceDtoFromExternalItemDto(ExternalItemDto sourceProviderExternalItem) {
        if (sourceProviderExternalItem != null) {
            RelatedResourceDto sourceProvider = new RelatedResourceDto();
            sourceProvider.setCode(sourceProviderExternalItem.getCode());
            sourceProvider.setCodeNested(sourceProviderExternalItem.getCode());
            sourceProvider.setId(sourceProviderExternalItem.getId());
            sourceProvider.setTitle(sourceProviderExternalItem.getTitle());
            sourceProvider.setUrn(sourceProviderExternalItem.getUrn());
            sourceProvider.setUrnProvider(sourceProviderExternalItem.getUrnProvider());
            return sourceProvider;
        }
        return null;

    }
}
