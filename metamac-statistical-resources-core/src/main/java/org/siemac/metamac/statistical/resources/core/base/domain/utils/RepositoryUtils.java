package org.siemac.metamac.statistical.resources.core.base.domain.utils;

public class RepositoryUtils {

//@formatter:off
    public static final String isLastPublishedVersionConditions =
        "( " +
            " stat.proc_status = :publishedProcStatus " +
            "AND stat.Valid_From <= :now " +
            "AND (stat.Valid_To > :now or stat.Valid_To is null)" +
        ")";
    //@formatter:on

    public static String buildLastPublishedVersionCondition(String statisticalResourceName) {
        // @formatter:off
        return "( " +
        " "+statisticalResourceName+".proc_status = :publishedProcStatus " +
        "AND "+statisticalResourceName+".Valid_From <= :now " +
        "AND ("+statisticalResourceName+".Valid_To > :now or "+statisticalResourceName+".Valid_To is null)" +
        ")";
        //@formatter:on

    }

}
