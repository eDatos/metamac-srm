package org.siemac.metamac.srm.core.category.serviceapi.utils;

import java.util.Date;

import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeVisualisationDto;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacDtoMocks;

import com.arte.statistic.sdmx.srm.core.category.serviceapi.utils.CategoriesDtoMocks;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class CategoriesMetamacDtoMocks {

    // -----------------------------------------------------------------------------------
    // CATEGORY SCHEMES
    // -----------------------------------------------------------------------------------

    public static CategorySchemeMetamacDto mockCategorySchemeDto(String codeMaintainer, String urnMaintainer) {
        CategorySchemeMetamacDto categorySchemeDto = new CategorySchemeMetamacDto();
        CategoriesDtoMocks.mockCategorySchemeDto(categorySchemeDto);
        categorySchemeDto.setMaintainer(OrganisationsMetamacDtoMocks.mockMaintainerDto(codeMaintainer, urnMaintainer));

        return categorySchemeDto;
    }

    // -----------------------------------------------------------------------------------
    // CATEGORIES
    // -----------------------------------------------------------------------------------

    public static CategoryMetamacDto mockCategoryDto() {
        CategoryMetamacDto categoryMetamacDto = new CategoryMetamacDto();
        CategoriesDtoMocks.mockCategoryDto(categoryMetamacDto);
        return categoryMetamacDto;
    }

    // -----------------------------------------------------------------------------------
    // CATEGORY ELEMENTS
    // -----------------------------------------------------------------------------------
    public static CategoryElementDto mockCategoryElementDto() {
        CategoryElementDto categoryElementDto = new CategoryElementDto();
        categoryElementDto.setCode("code-" + MetamacMocks.mockString(10));
        categoryElementDto.setShortName(MetamacMocks.mockInternationalStringDto("es", "shortName" + MetamacMocks.mockString(10)));
        categoryElementDto.setComment(MetamacMocks.mockInternationalStringDto("es", "comment" + MetamacMocks.mockString(10)));
        categoryElementDto.setValidFrom(new Date());
        categoryElementDto.setValidTo(new Date());
        return categoryElementDto;
    }

    // -----------------------------------------------------------------------------------
    // ORDER VISUALISATIONS
    // -----------------------------------------------------------------------------------

    public static CategorySchemeVisualisationDto mockCategorySchemeOrderVisualisationDto() {
        CategorySchemeVisualisationDto categorySchemeOrderVisualisationDto = new CategorySchemeVisualisationDto();
        categorySchemeOrderVisualisationDto.setCode("category-" + MetamacMocks.mockString(10));
        categorySchemeOrderVisualisationDto.setName(MetamacMocks.mockInternationalStringDto());
        return categorySchemeOrderVisualisationDto;
    }

    public static RelatedResourceDto mockCategorySchemeOrderVisualisationRelatedResourceDto(String code, String urn) {
        RelatedResourceDto relatedResourceDto = new RelatedResourceDto();
        relatedResourceDto.setCode(code);
        relatedResourceDto.setUrn(urn);
        relatedResourceDto.setUrnProvider(urn);
        relatedResourceDto.setType(null);
        return relatedResourceDto;
    }
}
