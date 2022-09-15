package org.siemac.metamac.srm.core.master.serviceapi;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
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
    protected MasterTablesService masterTableService;

    @Override
    @Test
    @Transactional
    public void testFindAllEniSchemaCatalogs() throws MetamacException {
        List<EniSchemaCatalog> eniSchemas = masterTableService.findAllEniSchemaCatalogs(getServiceContextAdministrador());
        assertTrue(!eniSchemas.isEmpty());
    }

    @Override
    @Test
    @Transactional
    public void testFindEniSchemaCatalogById() throws Exception {
        final Long ENI_SCHEMA_CATALOG_VALUE = 2L;
        EniSchemaCatalog eniSchema = masterTableService.findEniSchemaCatalogById(getServiceContextAdministrador(), ENI_SCHEMA_CATALOG_VALUE);
        assertNotNull(eniSchema);

    }
    
    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCategoriesTest.xml";
    }
    
}
