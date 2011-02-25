/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

 This file is part of jbilling.

 jbilling is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 jbilling is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sapienter.jbilling.client.pricing.util

import com.sapienter.jbilling.server.pricing.PriceModelWS
import org.codehaus.groovy.grails.web.metaclass.BindDynamicMethod
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

/**
 * PlanHelper 
 *
 * @author Brian Cowdery
 * @since 23/02/11
 */
class PlanHelper {

    static def PriceModelWS bindPriceModel(GrailsParameterMap params) {
        // sort price model parameters by index
        def sorted = new TreeMap<Integer, GrailsParameterMap>()
        params.model.each{ k, v ->
            if (v instanceof Map)
                sorted.put(k, v)
        }

        // build price model chain
        def root = null
        def model = null

        sorted.each{ i, modelParams ->
            if (model == null) {
                model = root = new PriceModelWS()
            } else {
                model = model.next = new PriceModelWS()
            }

            // bind model (can't use bindData() since this isn't a controller)
            def args =  [ model, modelParams, [exclude:[]] ]
            new BindDynamicMethod().invoke(model, 'bind', (Object[]) args)

            // bind model attributes
            modelParams.attribute.each{ j, attrParams ->
                if (attrParams instanceof Map)
                    if (attrParams.name)
                        model.attributes.put(attrParams.name, attrParams.value)
            }
        }

        return root;
    }

}
