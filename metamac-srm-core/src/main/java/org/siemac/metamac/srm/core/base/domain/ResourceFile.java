package org.siemac.metamac.srm.core.base.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity representing ResourceFile.
 * <p>
 * This class is responsible for the domain object related
 * business logic for ResourceFile. Properties and associations are
 * implemented in the generated base class {@link org.siemac.metamac.srm.core.base.domain.ResourceFileBase}.
 */
@Entity
@Table(name = "TB_RESOURCE_FILE")
public class ResourceFile extends ResourceFileBase {
    private static final long serialVersionUID = 1L;

    public ResourceFile() {
    }
}
