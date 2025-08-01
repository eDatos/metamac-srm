package org.siemac.metamac.srm.core.concept.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.srm.core.code.domain.ConceptMetamacResultSelection;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.concept.serviceapi.utils.ConceptsAsserts;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class ConceptsMetamacRepositoryTest extends SrmBaseTest {

    @Autowired
    protected ConceptMetamacRepository conceptMetamacRepository;

    @Test
    public void testRetrieveConceptsOrderedByCodelistUrn() throws Exception {

        ConceptMetamacResultSelection selection = new ConceptMetamacResultSelection(true, true, true, true, true);
        // Retrieve
        List<ItemResult> codes = conceptMetamacRepository.findConceptsByConceptSchemeOrderedInDepth(Long.valueOf(12), Integer.valueOf(2), selection);

        // Validate common metadata
        assertEquals(8, codes.size());
        int i = 0;
        {
            // Code 01 (validate all metadata)
            ItemResult code = codes.get(i++);
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_1, code.getUrnProvider());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_1, code.getUrn());
            assertEquals(null, code.getUriProvider());
            assertEquals("CONCEPT01", code.getCode());
            assertEquals(null, code.getCodeFull());
            assertEquals(null, code.getParent());
            assertEquals(2, code.getName().size());
            ConceptsAsserts.assertEqualsInternationalString(code.getName(), "es", "Nombre conceptScheme-1-v2-concept-1", "en", "Name conceptScheme-1-v2-concept-1");
            ConceptsAsserts.assertEqualsInternationalString(code.getDescription(), "es", "Descripción conceptScheme-1-v2-concept-1", "en", "description conceptScheme-1-v2-concept-1");
            assertEquals(Integer.valueOf(0), SrmServiceUtils.getConceptItemResultOrder(code));

        }
        {
            // Concept 02
            ItemResult code = codes.get(i++);
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2, code.getUrn());
            assertEquals("CONCEPT02", code.getCode());
            assertEquals(2, code.getName().size());
            ConceptsAsserts.assertEqualsInternationalString(code.getName(), "es", "Nombre conceptScheme-1-v2-concept-2", "en", "Name conceptScheme-1-v2-concept-2");
            ConceptsAsserts.assertEqualsInternationalString(code.getDescription(), null, null, null, null);
            assertEquals(Integer.valueOf(1), SrmServiceUtils.getConceptItemResultOrder(code));
            assertEquals(0, code.getAnnotations().size());
        }
        {
            // Concept 02 01
            ItemResult code = codes.get(i++);
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1, code.getUrn());
            assertEquals("CONCEPT02", code.getParent().getCode());
            assertEquals(null, code.getCodeFull());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2, code.getParent().getUrn());
            ConceptsAsserts.assertEqualsInternationalString(code.getName(), "es", "Nombre conceptScheme-1-v2-concept-2-1", "en", "Name conceptScheme-1-v2-concept-2-1");
            assertEquals(Integer.valueOf(0), SrmServiceUtils.getConceptItemResultOrder(code));

        }
        {
            // Concept 02 01 01
            ItemResult code = codes.get(i++);
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_2_1_1, code.getUrn());
            assertEquals("CONCEPT0201", code.getParent().getCode());
            ConceptsAsserts.assertEqualsInternationalString(code.getName(), "es", "Nombre conceptScheme-1-v2-concept-2-1-1", "en", "Name conceptScheme-1-v2-concept-2-1-1");
            assertEquals(Integer.valueOf(0), SrmServiceUtils.getConceptItemResultOrder(code));
        }

        {
            // Concept 03
            ItemResult code = codes.get(i++);
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_3, code.getUrn());
            ConceptsAsserts.assertEqualsInternationalString(code.getName(), "es", "nombre concept-3", "en", "name concept-3");
            assertEquals(Integer.valueOf(2), SrmServiceUtils.getConceptItemResultOrder(code));

        }
        {
            // Concept 04
            ItemResult code = codes.get(i++);
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4, code.getUrn());
            ConceptsAsserts.assertEqualsInternationalString(code.getName(), "es", "nombre concept-4", "en", "name concept-4");
            assertEquals(Integer.valueOf(3), SrmServiceUtils.getConceptItemResultOrder(code));
        }
        {
            // Concept 04 01
            ItemResult code = codes.get(i++);
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1, code.getUrn());
            ConceptsAsserts.assertEqualsInternationalString(code.getName(), "es", "nombre concept 4-1", "en", "name concept 4-1");
            assertEquals(Integer.valueOf(0), SrmServiceUtils.getConceptItemResultOrder(code));
        }
        {
            // Code 04 01 01
            ItemResult code = codes.get(i++);
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1_1, code.getUrn());
            assertEquals("CONCEPT0401", code.getParent().getCode());
            assertEquals(CONCEPT_SCHEME_1_V2_CONCEPT_4_1, code.getParent().getUrn());
            ConceptsAsserts.assertEqualsInternationalString(code.getName(), "es", "Nombre conceptScheme-1-v2-concept-4-1-1", "en", "Name conceptScheme-1-v2-concept-4-1-1");
            assertEquals(Integer.valueOf(0), SrmServiceUtils.getConceptItemResultOrder(code));
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmConceptsTest.xml";
    }
}
