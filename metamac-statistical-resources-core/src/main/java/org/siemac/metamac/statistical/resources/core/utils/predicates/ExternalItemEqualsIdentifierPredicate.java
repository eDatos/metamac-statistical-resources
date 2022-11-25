package org.siemac.metamac.statistical.resources.core.utils.predicates;

import org.siemac.metamac.core.common.util.predicates.MetamacPredicate;
import org.siemac.metamac.statistical.resources.core.common.domain.ExternalItem;

public class ExternalItemEqualsIdentifierPredicate extends MetamacPredicate<ExternalItem> {

    private final String identifier;

    public ExternalItemEqualsIdentifierPredicate(String identifier) {
        this.identifier = identifier;
    }

    @Override
    protected boolean eval(ExternalItem externalItem) {
        return identifier.equals(externalItem.getCode());
    }

}
