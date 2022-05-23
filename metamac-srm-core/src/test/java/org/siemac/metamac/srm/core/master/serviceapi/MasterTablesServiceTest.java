package org.siemac.metamac.srm.core.master.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.siemac.metamac.srm.core.master.dto.EniSchemaCatalogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class MasterTablesServiceTest extends SrmBaseTest
    implements MasterTablesServiceTestBase {
    @Autowired
    protected MasterTablesService masterTablesService;

    
    @Test
    public void testFindAllEniSchemaCatalogs() throws Exception {
        List<EniSchemaCatalogDto> eniSchemas = masterTablesService.findAllEniSchemaCatalogs(getServiceContextAdministrador());
        assertTrue("Must be, at least, one value for eni_schema_catalogs", (eniSchemas != null ? eniSchemas.size() : 0L) > 0L);

        
    }

    @Test
    public void testFindEniSchemaCatalogById() throws Exception {
        final Long ENI_SCHEMA_CATALOG_VALUE = 2L;
        EniSchemaCatalog eniSchema = masterTablesService.findEniSchemaCatalogById(getServiceContextAdministrador(), ENI_SCHEMA_CATALOG_VALUE);
        assertEquals(2L, eniSchema != null ? eniSchema.getId() : 0L);
        
    }
    
    @Override
    protected String getDataSetFile() {
        return "";
    }
}
