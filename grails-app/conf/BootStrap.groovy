import static jbilling.FilterConstraint.*
import static jbilling.FilterType.*
import jbilling.Filter

class BootStrap {

    def init = { servletContext ->
        createFilters();
    }
    def destroy = {
    }

    void createFilters() {
        if (Filter.list().isEmpty()) {
            // global filters
            new Filter(type: ALL, constraintType: EQ, field: "id", template: "id").save();
            new Filter(type: ALL, constraintType: EQ, field: "status", template: "status").save();
            new Filter(type: ALL, constraintType: DATE_BETWEEN, field: "createDatetime", template: "created").save();

            // customer filters
            new Filter(type: CUSTOMER, constraintType: EQ, field: "userName", template: "customer/login").save();
            new Filter(type: CUSTOMER, constraintType: EQ, field: "contact.email", template: "customer/email").save();
            
            // order filters

            // invoice filters
        }
    }
}
