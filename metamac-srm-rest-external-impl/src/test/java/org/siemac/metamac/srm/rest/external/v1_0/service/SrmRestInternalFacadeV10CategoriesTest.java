package org.siemac.metamac.srm.rest.external.v1_0.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_ALL;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_LATEST;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.FIELD_ENI_SCHEME;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ITEM_2_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.QUERY_LATEST;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.VERSION_1;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.VERSION_2;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategoryScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategorySchemes;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacProperties;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.external.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.external.v1_0.category.utils.CategoriesDoMocks;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResultSelection;

public class SrmRestInternalFacadeV10CategoriesTest extends SrmRestInternalFacadeV10BaseTest {

    private CategoriesMetamacService    categoriesService;
    private ItemSchemeVersionRepository itemSchemeVersionRepository;

    @Test
    public void testJsonAcceptable() throws Exception {

        String requestUri = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);

        // Request and validate
        WebClient webClient = WebClient.create(requestUri).accept("application/json");
        Response response = webClient.get();

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testFindCategorySchemes() throws Exception {
        testFindCategorySchemes(null, null, null, null, null, null, null); // without limits
        testFindCategorySchemes(null, null, null, "10000", null, null, null); // without limits
        testFindCategorySchemes(null, null, null, null, "0", null, null); // without limits, first page
        testFindCategorySchemes(null, null, null, "2", "0", null, null); // first page with pagination
        testFindCategorySchemes(null, null, null, "2", "2", null, null); // other page with pagination
        testFindCategorySchemes(null, null, null, null, null, QUERY_ID_LIKE_1, null); // query by id, without limits
        testFindCategorySchemes(null, null, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, without limits
        testFindCategorySchemes(null, null, null, null, null, QUERY_LATEST, null); // latest
        testFindCategorySchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, first page
        testFindCategorySchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query by id and name, first page
    }

    @Test
    public void testFindCategorySchemesXml() throws Exception {
        String requestUri = getUriItemSchemes(null, null, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/findCategorySchemes.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindCategorySchemesByAgency() throws Exception {
        testFindCategorySchemes(AGENCY_1, null, null, null, null, null, null);
        testFindCategorySchemes(AGENCY_1, null, null, null, "0", null, null);
        testFindCategorySchemes(AGENCY_1, null, null, "2", "0", null, null);
        testFindCategorySchemes(AGENCY_1, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindCategorySchemes(AGENCY_1, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindCategorySchemesByAgencyTestLinks() throws Exception {
        String agencyID = AGENCY_1;
        CategorySchemes categorySchemes = getSrmRestInternalFacadeClientXml().findCategorySchemes(agencyID, null, null, "4", "4");
        assertEquals(getApiEndpoint() + "/categoryschemes/" + agencyID + "?limit=4&offset=4", categorySchemes.getSelfLink());
        assertEquals(getApiEndpoint() + "/categoryschemes/" + agencyID + "?limit=4&offset=0", categorySchemes.getFirstLink());
        assertEquals(getApiEndpoint() + "/categoryschemes/" + agencyID + "?limit=4&offset=0", categorySchemes.getPreviousLink());
        assertEquals(getApiEndpoint() + "/categoryschemes/" + agencyID + "?limit=4&offset=36", categorySchemes.getLastLink());
        assertEquals(SrmRestConstants.KIND_CATEGORY_SCHEMES, categorySchemes.getKind());
    }
    @Test
    public void testFindCategorySchemesByAgencyErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findCategorySchemes(WILDCARD_ALL, null, null, null, null);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter agencyID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_AGENCY_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testFindCategorySchemesByAgencyAndResource() throws Exception {
        testFindCategorySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindCategorySchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, null, null, null, null);
        testFindCategorySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", null, null, null);
        testFindCategorySchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, "2", null, null, null);
        testFindCategorySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindCategorySchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
        testFindCategorySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
        testFindCategorySchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindCategorySchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindCategorySchemes(WILDCARD_ALL, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindCategorySchemesByAgencyAndResourceTestLinks() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        CategorySchemes categorySchemes = getSrmRestInternalFacadeClientXml().findCategorySchemes(agencyID, resourceID, null, null, "4", "4");
        assertEquals(getApiEndpoint() + "/categoryschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=4", categorySchemes.getSelfLink());
        assertEquals(getApiEndpoint() + "/categoryschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=0", categorySchemes.getFirstLink());
        assertEquals(getApiEndpoint() + "/categoryschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=0", categorySchemes.getPreviousLink());
        assertEquals(getApiEndpoint() + "/categoryschemes/" + agencyID + "/" + resourceID + "?limit=4&offset=36", categorySchemes.getLastLink());
        assertEquals(SrmRestConstants.KIND_CATEGORY_SCHEMES, categorySchemes.getKind());
    }

    @Test
    public void testFindCategorySchemesByAgencyAndResourceErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findCategorySchemes(AGENCY_1, WILDCARD_ALL, null, null, null, null);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter resourceID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_RESOURCE_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveCategoryScheme() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;
        CategoryScheme categoryScheme = getSrmRestInternalFacadeClientXml().retrieveCategoryScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(categoryScheme);
        // other metadata are tested in mapper tests
        assertEquals(agencyID, categoryScheme.getAgencyID());
        assertEquals(resourceID, categoryScheme.getId());
        assertEquals(version, categoryScheme.getVersion());
        assertEquals(SrmRestConstants.KIND_CATEGORY_SCHEME, categoryScheme.getKind());
        assertEquals(SrmRestConstants.KIND_CATEGORY_SCHEME, categoryScheme.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_CATEGORY_SCHEMES, categoryScheme.getParentLink().getKind());
    }

    @Test
    public void testRetrieveCategorySchemeVersionLatest() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = WILDCARD_LATEST;
        CategoryScheme categoryScheme = getSrmRestInternalFacadeClientXml().retrieveCategoryScheme(agencyID, resourceID, version);

        // Validation
        assertNotNull(categoryScheme);
        // other metadata are tested in mapper tests
        assertEquals(agencyID, categoryScheme.getAgencyID());
        assertEquals(resourceID, categoryScheme.getId());
        assertEquals(VERSION_1, categoryScheme.getVersion());
        assertEquals(SrmRestConstants.KIND_CATEGORY_SCHEME, categoryScheme.getKind());
        assertEquals(SrmRestConstants.KIND_CATEGORY_SCHEME, categoryScheme.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_CATEGORY_SCHEMES, categoryScheme.getParentLink().getKind());
    }

    @Test
    public void testRetrieveCategorySchemeXml() throws Exception {

        String requestBase = getUriItemSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};

        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/retrieveCategoryScheme.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveCategorySchemeErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = NOT_EXISTS;
        String version = VERSION_1;
        try {
            getSrmRestInternalFacadeClientXml().retrieveCategoryScheme(agencyID, resourceID, version);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.CATEGORY_SCHEME_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("CategoryScheme " + resourceID + " not found in version " + version + " from Agency " + agencyID, exception.getMessage());
            assertEquals(3, exception.getParameters().getParameters().size());
            assertEquals(resourceID, exception.getParameters().getParameters().get(0));
            assertEquals(version, exception.getParameters().getParameters().get(1));
            assertEquals(agencyID, exception.getParameters().getParameters().get(2));
            assertNull(exception.getErrors());
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveCategorySchemeErrorNotExistsXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, NOT_EXISTS, VERSION_1);
        InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/retrieveCategoryScheme.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveCategorySchemeErrorWildcard() throws Exception {
        // Agency
        try {
            getSrmRestInternalFacadeClientXml().retrieveCategoryScheme(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter agencyID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_AGENCY_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }

        // Resource
        try {
            getSrmRestInternalFacadeClientXml().retrieveCategoryScheme(AGENCY_1, WILDCARD_ALL, VERSION_1);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter resourceID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_RESOURCE_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }

        // Version
        try {
            getSrmRestInternalFacadeClientXml().retrieveCategoryScheme(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter version has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_VERSION, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }
    @Test
    public void testFindCategories() throws Exception {

        // without parameters
        testFindCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, null, null, null, null); // without limits
        testFindCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "10000", null, null, null, null); // without limits
        testFindCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, "0", null, null, null); // without limits, first page
        testFindCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", null, null, null); // with pagination
        testFindCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null, null); // query
        testFindCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC, null); // query and order
        testFindCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", null, null, FIELD_ENI_SCHEME); // with eni scheme
        testFindCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, "2", "0", null, null, FIELD_ENI_SCHEME); // with eni scheme

        // agency
        testFindCategories(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, null, null, null, null, null); // without limits
        testFindCategories(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "10000", null, null, null, null); // without limits
        testFindCategories(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, null, "0", null, null, null); // without limits, first page
        testFindCategories(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", null, null, null); // with pagination
        testFindCategories(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null, null); // query
        testFindCategories(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC, null); // query and order
        testFindCategories(AGENCY_1, WILDCARD_ALL, WILDCARD_ALL, "2", "0", null, null, FIELD_ENI_SCHEME); // with eni scheme

        // resource
        testFindCategories(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, null, null, null, null, null); // without limits
        testFindCategories(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "10000", null, null, null, null); // without limits
        testFindCategories(WILDCARD_ALL, ITEM_SCHEME_1_CODE, WILDCARD_ALL, null, "0", null, null, null); // without limits, first page
        testFindCategories(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", null, null, null); // with pagination
        testFindCategories(WILDCARD_ALL, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null, null); // query
        testFindCategories(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC, null); // query and order
        testFindCategories(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, "2", "0", null, null, FIELD_ENI_SCHEME); // with eni scheme

        // version
        testFindCategories(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, "2", "0", null, null, null); // with pagination
        testFindCategories(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null, null); // query
        testFindCategories(AGENCY_1, WILDCARD_ALL, VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC, null); // query and order
        testFindCategories(AGENCY_1, WILDCARD_ALL, VERSION_1, "2", "0", null, null, FIELD_ENI_SCHEME); // with eni scheme
    }

    @Test
    public void testFindCategoriesWithCategoryElementExplicit() throws Exception {
        resetMocks();
        Categories categories = getSrmRestInternalFacadeClientXml().findCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, null, null, null, null, "+categoryElement", null);
        assertNotNull(categories.getCategories().get(0).getCategoryElement());
    }

    @Test
    public void testFindCategoriesWithoutCategoryElementExplicit() throws Exception {
        resetMocks();
        Categories categories = getSrmRestInternalFacadeClientXml().findCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, null, null, null, null, null);
        assertNull(categories.getCategories().get(0).getCategoryElement());
    }

    @Test
    public void testFindCategoriesWithCategoryElementXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, QUERY_ID_LIKE_1_NAME_LIKE_2, "4", "4", "+categoryElement");
        InputStream responseExpected = SrmRestInternalFacadeV10CodesTest.class.getResourceAsStream("/responses/categories/findCategoriesWithCategoryElements.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindCategoriesXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, QUERY_ID_LIKE_1_NAME_LIKE_2, "4", "4", null);
        InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/findCategories.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindCategoriesRetrieveTsv() throws Exception {

        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, null, null, null, null);
        InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/findCategoriesTSV.tsv");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, "text/tab-separated-values", Status.OK, responseExpected);

    }

    @Test
    public void testFindCategoriesTsv() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, null, null, null, null);
        List<String> lines = getResponse(requestUri, "text/tab-separated-values");

        Iterator<String> iterator = lines.iterator();

        assertEquals("code\tparent\turn\tname#es\tname#en", iterator.next());
        assertTrue(lines.contains("category1\t\turn:sdmx:org.sdmx.infomodel.categoryscheme.Category=agency1:itemScheme1(01.000).category1\tname-category1 en Español\tname-category1 in English"));
        assertTrue(lines.contains("category2\t\turn:sdmx:org.sdmx.infomodel.categoryscheme.Category=agency1:itemScheme1(01.000).category2\tname-category2 en Español\tname-category2 in English"));

    }

    @Test
    public void testFindCategoriesRetrieveCsv() throws Exception {

        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, null, null, null, null);
        InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/findCategoriesCSV.csv");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, "text/csv", Status.OK, responseExpected);

    }

    @Test
    public void testFindCategoriesCsv() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, null, null, null, null);
        List<String> lines = getResponse(requestUri, "text/csv");

        Iterator<String> iterator = lines.iterator();

        assertEquals("code,parent,urn,name#es,name#en", iterator.next());
        assertTrue(lines.contains("category1,,urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=agency1:itemScheme1(01.000).category1,name-category1 en Español,name-category1 in English"));
        assertTrue(lines.contains("category2,,urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=agency1:itemScheme1(01.000).category2,name-category2 en Español,name-category2 in English"));
    }

    @Test
    public void testFindCategoriesRetrieveAll() throws Exception {

        resetMocks();
        Categories categories = getSrmRestInternalFacadeClientXml().findCategories(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, null, null, null, null, null, null);

        assertNotNull(categories);
        assertEquals(SrmRestConstants.KIND_CATEGORIES, categories.getKind());
        assertEquals(BigInteger.valueOf(4), categories.getTotal());

        // Verify with mockito
        ArgumentCaptor<String> categorySchemeUrnArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ItemResultSelection> itemResultSelectionArgument = ArgumentCaptor.forClass(ItemResultSelection.class);
        ArgumentCaptor<String> orderArgument = ArgumentCaptor.forClass(String.class);
        verify(categoriesService).retrieveCategoriesByCategorySchemeUrnOrderedInDepth(any(ServiceContext.class), categorySchemeUrnArgument.capture(), itemResultSelectionArgument.capture(),
                orderArgument.capture());
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=agency1:itemScheme1(01.000)", categorySchemeUrnArgument.getValue());
        assertEquals(true, itemResultSelectionArgument.getValue().isNames());
        assertEquals(false, itemResultSelectionArgument.getValue().isDescriptions());
        assertEquals(false, itemResultSelectionArgument.getValue().isComments());
        assertEquals(false, itemResultSelectionArgument.getValue().isAnnotations());
    }

    @Test
    public void testFindCategoriesRetrieveAllXml() throws Exception {
        String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, null, null, null, null);
        InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/findCategoriesRetrieveAll.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testRetrieveCategory() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;
        String categoryID = ITEM_1_CODE;
        Category category = getSrmRestInternalFacadeClientXml().retrieveCategory(agencyID, resourceID, version, categoryID, null);

        // Validation
        assertNotNull(category);
        assertEquals(categoryID, category.getId());
        assertEquals(SrmRestConstants.KIND_CATEGORY, category.getKind());
        assertEquals(SrmRestConstants.KIND_CATEGORY, category.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_CATEGORIES, category.getParentLink().getKind());
        assertTrue(category instanceof Category);
        // other metadata are tested in transformation tests
    }

    @Test
    public void testRetrieveCategoryXml() throws Exception {

        String requestBase = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, ITEM_1_CODE);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};
        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/retrieveCategory.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveCategoryWithCategoryElementExplicit() throws Exception {
        String fields = "fields=+categoryElement";
        String requestBase = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, ITEM_1_CODE);
        String[] requestUris = getRequestUri(requestBase, fields);
        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/retrieveCategory.id2.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveCategoryErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ITEM_SCHEME_1_CODE;
        String version = VERSION_1;
        String categoryID = NOT_EXISTS;
        try {
            getSrmRestInternalFacadeClientXml().retrieveCategory(agencyID, resourceID, version, categoryID, null);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.CATEGORY_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("Category " + categoryID + " not found in version " + version + " of CategoryScheme " + resourceID + " from Agency " + agencyID, exception.getMessage());
            assertEquals(4, exception.getParameters().getParameters().size());
            assertEquals(categoryID, exception.getParameters().getParameters().get(0));
            assertEquals(version, exception.getParameters().getParameters().get(1));
            assertEquals(resourceID, exception.getParameters().getParameters().get(2));
            assertEquals(agencyID, exception.getParameters().getParameters().get(3));
            assertNull(exception.getErrors());
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveCategoryErrorNotExistsXml() throws Exception {
        String requestUri = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, NOT_EXISTS);
        InputStream responseExpected = SrmRestInternalFacadeV10CategoriesTest.class.getResourceAsStream("/responses/categories/retrieveCategory.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveCategoryErrorWildcard() throws Exception {

        // AgencyID
        try {
            getSrmRestInternalFacadeClientXml().retrieveCategory(WILDCARD_ALL, ITEM_SCHEME_1_CODE, VERSION_1, ITEM_1_CODE, null);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter agencyID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_AGENCY_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
        // AgencyID
        try {
            getSrmRestInternalFacadeClientXml().retrieveCategory(AGENCY_1, WILDCARD_ALL, VERSION_1, ITEM_1_CODE, null);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter resourceID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_RESOURCE_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }

        // AgencyID
        try {
            getSrmRestInternalFacadeClientXml().retrieveCategory(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD_ALL, ITEM_1_CODE, null);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter version has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_VERSION, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }

        // ItemID
        try {
            getSrmRestInternalFacadeClientXml().retrieveCategory(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, WILDCARD_ALL, null);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter categoryID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_CATEGORY_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    private void testFindCategorySchemes(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();

        // Find
        CategorySchemes categorySchemes = null;
        if (agencyID == null) {
            categorySchemes = getSrmRestInternalFacadeClientXml().findCategorySchemes(query, orderBy, limit, offset);
        } else if (resourceID == null) {
            categorySchemes = getSrmRestInternalFacadeClientXml().findCategorySchemes(agencyID, query, orderBy, limit, offset);
        } else {
            categorySchemes = getSrmRestInternalFacadeClientXml().findCategorySchemes(agencyID, resourceID, query, orderBy, limit, offset);
        }

        assertNotNull(categorySchemes);
        assertEquals(SrmRestConstants.KIND_CATEGORY_SCHEMES, categorySchemes.getKind());
    }

    private void testFindCategories(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy, String fields) throws Exception {
        resetMocks();
        Categories categories = getSrmRestInternalFacadeClientXml().findCategories(agencyID, resourceID, version, query, orderBy, limit, offset, null, fields, null);

        assertNotNull(categories);
        assertEquals(SrmRestConstants.KIND_CATEGORIES, categories.getKind());

        if (fields != null && fields.contains(SrmRestConstants.FIELD_INCLUDE_ENI_SCHEME)) {
            for (int i = 0; i < categories.getCategories().size(); i++) {
                assertNotNull(categories.getCategories().get(i).getEniSchemaTypes());
            }
        } else {
            for (int i = 0; i < categories.getCategories().size(); i++) {
                assertNull(categories.getCategories().get(i).getEniSchemaTypes());
            }
        }

    }

    private void mockRetrieveItemSchemeVersionByVersion() throws MetamacException {
        when(itemSchemeVersionRepository.retrieveByVersion(any(Long.class), any(String.class))).thenAnswer(new Answer<ItemSchemeVersion>() {

            @Override
            public ItemSchemeVersion answer(InvocationOnMock invocation) throws Throwable {
                String version = (String) invocation.getArguments()[1];
                return CategoriesDoMocks.mockCategoryScheme("agencyID", version, version);
            };
        });
    }

    @SuppressWarnings("unchecked")
    private void mockFindCategorySchemesByCondition() throws MetamacException {
        when(categoriesService.findCategorySchemesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class)))
                .thenAnswer(new Answer<PagedResult<CategorySchemeVersionMetamac>>() {

                    @Override
                    public org.fornax.cartridges.sculptor.framework.domain.PagedResult<CategorySchemeVersionMetamac> answer(InvocationOnMock invocation) throws Throwable {
                        List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];
                        String agencyID = getAgencyIdFromConditionalCriteria(conditions, CategorySchemeVersionMetamacProperties.maintainableArtefact());
                        String resourceID = getItemSchemeIdFromConditionalCriteria(conditions, CategorySchemeVersionMetamacProperties.maintainableArtefact());
                        String version = getVersionFromConditionalCriteria(conditions, CategorySchemeVersionMetamacProperties.maintainableArtefact());
                        Boolean latest = getVersionLatestFromConditionalCriteria(conditions, CategorySchemeVersionMetamacProperties.maintainableArtefact());
                        if (agencyID != null && resourceID != null && (version != null || Boolean.TRUE.equals(latest))) {
                            // Retrieve one
                            CategorySchemeVersionMetamac categorySchemeVersion = null;
                            if (NOT_EXISTS.equals(agencyID) || NOT_EXISTS.equals(resourceID) || NOT_EXISTS.equals(version)) {
                                categorySchemeVersion = null;
                            } else if (AGENCY_1.equals(agencyID) && ITEM_SCHEME_1_CODE.equals(resourceID) && (VERSION_1.equals(version) || Boolean.TRUE.equals(latest))) {
                                categorySchemeVersion = CategoriesDoMocks.mockCategorySchemeWithCategories(agencyID, resourceID, VERSION_1);
                            } else {
                                fail();
                            }
                            List<CategorySchemeVersionMetamac> categorySchemes = new ArrayList<CategorySchemeVersionMetamac>();
                            if (categorySchemeVersion != null) {
                                categorySchemes.add(categorySchemeVersion);
                            }
                            return new PagedResult<CategorySchemeVersionMetamac>(categorySchemes, 0, categorySchemes.size(), categorySchemes.size());
                        } else {
                            // any
                            List<CategorySchemeVersionMetamac> categorySchemes = new ArrayList<CategorySchemeVersionMetamac>();
                            categorySchemes.add(CategoriesDoMocks.mockCategorySchemeWithCategories(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1));
                            categorySchemes.add(CategoriesDoMocks.mockCategorySchemeWithCategories(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1));
                            categorySchemes.add(CategoriesDoMocks.mockCategorySchemeWithCategories(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_2));
                            categorySchemes.add(CategoriesDoMocks.mockCategorySchemeWithCategories(AGENCY_2, ITEM_SCHEME_3_CODE, VERSION_1));
                            return new PagedResult<CategorySchemeVersionMetamac>(categorySchemes, categorySchemes.size(), categorySchemes.size(), categorySchemes.size(), categorySchemes.size() * 10,
                                    0);
                        }
                    };
                });
    }

    @SuppressWarnings("unchecked")
    private void mockFindCategoriesByCondition() throws MetamacException {
        when(categoriesService.findCategoriesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(new Answer<PagedResult<CategoryMetamac>>() {

            @Override
            public org.fornax.cartridges.sculptor.framework.domain.PagedResult<CategoryMetamac> answer(InvocationOnMock invocation) throws Throwable {
                List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];

                String agencyID = getAgencyIdFromConditionalCriteria(conditions, CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact());
                String resourceID = getItemSchemeIdFromConditionalCriteria(conditions, CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact());
                String version = getVersionFromConditionalCriteria(conditions, CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact());
                String itemID = getItemIdFromConditionalCriteria(conditions, CategoryMetamacProperties.nameableArtefact());

                if (agencyID != null && resourceID != null && version != null && itemID != null) {
                    // Retrieve one
                    CategoryMetamac category = null;
                    if (NOT_EXISTS.equals(agencyID) || NOT_EXISTS.equals(resourceID) || NOT_EXISTS.equals(version) || NOT_EXISTS.equals(itemID)) {
                        category = null;
                    } else if (AGENCY_1.equals(agencyID) && ITEM_SCHEME_1_CODE.equals(resourceID) && VERSION_1.equals(version) && ITEM_1_CODE.equals(itemID)) {
                        CategorySchemeVersionMetamac categoryScheme1 = CategoriesDoMocks.mockCategoryScheme(AGENCY_1, resourceID, version);
                        CategoryMetamac parent = CategoriesDoMocks.mockCategoryWithEniScheme(ITEM_2_CODE, categoryScheme1, null);
                        category = CategoriesDoMocks.mockCategoryWithEniScheme(ITEM_1_CODE, categoryScheme1, parent);
                    } else {
                        fail();
                    }
                    List<CategoryMetamac> categories = new ArrayList<CategoryMetamac>();
                    if (category != null) {
                        categories.add(category);
                    }
                    return new PagedResult<CategoryMetamac>(categories, 0, categories.size(), categories.size());
                } else {
                    // any
                    CategorySchemeVersionMetamac categoryScheme1 = CategoriesDoMocks.mockCategoryScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);
                    CategorySchemeVersionMetamac categoryScheme2 = CategoriesDoMocks.mockCategoryScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1);

                    List<CategoryMetamac> categories = new ArrayList<CategoryMetamac>();
                    categories.add(CategoriesDoMocks.mockCategoryWithEniScheme("category1", categoryScheme1, null));
                    categories.add(CategoriesDoMocks.mockCategoryWithEniScheme("category2", categoryScheme1, null));
                    categories.add(CategoriesDoMocks.mockCategoryWithEniScheme("category2A", categoryScheme1, categories.get(1)));
                    categories.add(CategoriesDoMocks.mockCategoryWithEniScheme("category1", categoryScheme2, null));

                    return new PagedResult<CategoryMetamac>(categories, categories.size(), categories.size(), categories.size(), categories.size() * 10, 0);
                }
            };
        });
    }

    private void mockRetrieveCategoriesByCategoryScheme() throws MetamacException {
        when(categoriesService.retrieveCategoriesByCategorySchemeUrnOrderedInDepth(any(ServiceContext.class), any(String.class), any(ItemResultSelection.class), any(String.class)))
                .thenAnswer(new Answer<List<ItemResult>>() {

                    @Override
                    public List<ItemResult> answer(InvocationOnMock invocation) throws Throwable {
                        // any
                        ItemResult category1 = CategoriesDoMocks.mockCategoryResultFixedValuesWithExtraFields("category1", null);
                        ItemResult category2 = CategoriesDoMocks.mockCategoryResultFixedValuesWithExtraFields("category2", null);
                        ItemResult category2A = CategoriesDoMocks.mockCategoryResultFixedValuesWithExtraFields("category2A", category2);
                        ItemResult category2B = CategoriesDoMocks.mockCategoryResultFixedValuesWithExtraFields("category2B", category2);
                        return Arrays.asList(category1, category2, category2A, category2B);
                    };
                });
    }

    @Override
    protected void resetMocks() throws MetamacException {
        categoriesService = applicationContext.getBean(CategoriesMetamacService.class);
        reset(categoriesService);
        itemSchemeVersionRepository = applicationContext.getBean(ItemSchemeVersionRepository.class);
        reset(itemSchemeVersionRepository);

        mockRetrieveItemSchemeVersionByVersion();
        mockFindCategorySchemesByCondition();
        mockFindCategoriesByCondition();
        mockRetrieveCategoriesByCategoryScheme();
    }

    @Test
    public void testFindCategoriesWithLangSingleLanguage() throws Exception {
        resetMocks();
        Categories categories = getSrmRestInternalFacadeClientXml().findCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, "NAME ASC", "2", "0", null, Arrays.asList("ca"));
        assertNotNull(categories);
        assertEquals(SrmRestConstants.KIND_CATEGORIES, categories.getKind());
    }

    @Test
    public void testFindCategoriesWithLangMultipleLanguages() throws Exception {
        resetMocks();
        Categories categories = getSrmRestInternalFacadeClientXml().findCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, "NAME ASC", "2", "0", null, Arrays.asList("ca,es"));
        assertNotNull(categories);
        assertEquals(SrmRestConstants.KIND_CATEGORIES, categories.getKind());
    }

    @Test
    public void testFindCategoriesWithoutLang() throws Exception {
        resetMocks();
        Categories categories = getSrmRestInternalFacadeClientXml().findCategories(WILDCARD_ALL, WILDCARD_ALL, WILDCARD_ALL, null, "NAME ASC", "2", "0", null, null);
        assertNotNull(categories);
        assertEquals(SrmRestConstants.KIND_CATEGORIES, categories.getKind());
    }

    @Override
    protected String getSupathMaintainableArtefacts() {
        return "categoryschemes";
    }

    @Override
    protected String getSupathItems() {
        return "categories";
    }
}