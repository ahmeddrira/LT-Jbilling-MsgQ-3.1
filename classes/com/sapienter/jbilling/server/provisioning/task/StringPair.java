package com.sapienter.jbilling.server.provisioning.task;


public class StringPair {
    private String name;
    private String value;

    /**
     * @param name
     * @param value
     */
    public StringPair(String name, String value) {
        this.name  = name;
        this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /*
     *  (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "(" + name + "," + value + ")";
    }
}



// ~ Formatted by Jindent --- http://www.jindent.com


