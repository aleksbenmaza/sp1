/**
 * Created by alexandremasanes on 18/11/2017.
 */
@TypeDefs({
        @TypeDef(
                name = "namesType",
                typeClass = NamesType.class
        )
})
package org.aaa.core.business.mapping.entity;

import org.aaa.orm.custom.NamesType;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;