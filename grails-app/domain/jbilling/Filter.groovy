package jbilling

/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2010 Enterprise jBilling Software Ltd. and Emiliano Conde

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

/**
 * Filter

 * @author Brian Cowdery
 * @since  30-11-2010
 */
class Filter {

    static transients = [ "value", "name" ]

    static mapping = {
        id generator: 'org.hibernate.id.enhanced.TableGenerator',
           params: [
           table_name: 'jbilling_seqs',
           segment_column_name: 'name',
           value_column_name: 'next_id',
           segment_value: 'filter'
           ]
        filterSet column: 'filter_set_id'
    }

    static constraints = {
        stringValue(blank:true, nullable:true)
        integerValue(nullable:true)
        startDateValue(nullable:true)
        endDateValue(nullable:true)
    }

    static belongsTo = [filterSet: FilterSet]

    FilterType type
    FilterConstraint constraintType
    String field
    String template
    Boolean visible

    String stringValue
    Integer integerValue
    Date startDateValue
    Date endDateValue

    def Filter() {
    }

    def Filter(Filter filter) {
        this.type = filter.type
        this.constraintType = filter.constraintType
        this.field = filter.field
        this.template = filter.template
        this.visible = filter.visible
        this.stringValue = filter.stringValue
        this.integerValue = filter.integerValue
        this.startDateValue = filter.startDateValue
        this.endDateValue = filter.endDateValue
    }

    def Object getValue() {
        if (stringValue != null)
            return stringValue

        if (integerValue != null)
            return integerValue

        if (startDateValue != null)
            return startDateValue

        if (endDateValue != null)
            return endDateValue

        return null
    }

    public String getName() {
        return "${type}-${constraintType}_${field.replaceAll('\\.','_').capitalize()}"
    }

    def void clear() {
        stringValue = null
        integerValue = null
        startDateValue = null
        endDateValue = null
    }

    @Override
    def boolean equals(o) {
        if (this.is(o)) return true;
        if (getClass() != o.class) return false;

        Filter filter = (Filter) o;

        if (constraintType != filter.constraintType) return false;
        if (field != filter.field) return false;
        if (template != filter.template) return false;
        if (type != filter.type) return false;
        return true;
    }

    @Override
    def int hashCode() {
        int result;
        result = type.hashCode();
        result = 31 * result + constraintType.hashCode();
        result = 31 * result + field.hashCode();
        result = 31 * result + template.hashCode();
        return result;
    }

    @Override
    def String toString ( ) {
        return "Filter{id=${id}, type=${type}, constrainttype=${constraintType}, field=${field}}"
    }
}