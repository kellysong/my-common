package com.sjl.core.util;

import java.util.Collection;


public class CollectionUtils {

    public static boolean isEmpty(final Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }


    public static boolean isNotEmpty(final Collection<?> coll) {
        return !isEmpty(coll);
    }
}
