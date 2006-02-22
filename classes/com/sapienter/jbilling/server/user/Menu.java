/*
The contents of this file are subject to the Jbilling Public License
Version 1.1 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.jbilling.com/JPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is jbilling.

The Initial Developer of the Original Code is Emiliano Conde.
Portions created by Sapienter Billing Software Corp. are Copyright 
(C) Sapienter Billing Software Corp. All Rights Reserved.

Contributor(s): ______________________________________.
*/

package com.sapienter.jbilling.server.user;

import java.io.Serializable;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.SessionInternalError;

public class Menu implements Serializable {
    private Hashtable options;
    private Vector rootOptions; 
    private MenuOption selectedOption;
    private MenuOption selectedSubOption;
    private Vector subOptions;
    private Vector lmOptions;
    
    public Menu() {
        rootOptions = new Vector();
        subOptions = new Vector();
        lmOptions = new Vector();
        options = new Hashtable();
    }
    
    public Vector getOptions() {
        return rootOptions;
    }
    
    public Vector getSubOptions() {
        return subOptions;
    }
    
    public Vector getLMOptions() {
        return lmOptions;
    }
    
    protected void addOption(MenuOption option) 
            throws NamingException, FinderException{
        options.put(option.getId(), option);
    }
    

    // this has to be called after all the options are in the hashtable
    protected void init() throws SessionInternalError { 
        //Logger log = Logger.getLogger(Menu.class);
        for (Iterator it = options.values().iterator(); it.hasNext();) {
            MenuOption option = (MenuOption) it.next();

            if (option.getParentId() == null) {
                rootOptions.add(option);
                // I need something to be selected at any time ...
                if (selectedOption == null) {
                    selectedOption = option;
                    selectedOption.setSelected(new Boolean("true"));
                }
            } else {
                MenuOption parent = (MenuOption) options.get(
                        option.getParentId());
                        
                if (parent == null) {
                    throw new SessionInternalError("can't find parent " + option.getParentId() + " for " +                        "option" + option);
                }
                /*
                Logger.getLogger(Menu.class).debug("adding suboption " + option +
                                        " parent = " + parent);*/

                parent.getOptions().add(option.getId());
            }
        }   
        
        // set the suboptions according to the initially selected option
        setVector(selectedOption, subOptions);        

        // here the options are sorted, using the MenuOption compare method.
        // therefore, any option can be passed as the second parameter
        Collections.sort(rootOptions, selectedOption);
    }
    
    /**
     * 
     * @param id
     * @return true if a main option was selected, otherwise false
     * @throws SessionInternalError
     */
    public boolean selectOption(Integer id) 
            throws SessionInternalError {
        Logger log = Logger.getLogger(Menu.class);
        boolean retValue = false;
        MenuOption option = (MenuOption) options.get(id);
        if (option == null) {
            throw new SessionInternalError("Option not found in menu:" + id);
        }
        
        if (selectedOption.equals(option)) { //re-click
            return retValue;
        }
        
        int level = option.getLevel().intValue();
        switch (level) {
        case 1: // redefine the suboptions
            setVector(option, subOptions);
            // update who is selected and who's not
            selectedOption.setSelected(new Boolean(false));
            selectedOption = option;
            selectedOption.setSelected(new Boolean(true));
            if (selectedSubOption != null) {
                selectedSubOption.setSelected(new Boolean(false));
                selectedSubOption = null;
            }
            lmOptions.clear();
            retValue = true;
            log.debug("option selected now suboptions size = " + subOptions.size());
        break;
        case 2: // redefine the lf options
            setVector(option, lmOptions);
            // update the selected flag of the suboptions
            if (selectedSubOption != null) {
                selectedSubOption.setSelected(new Boolean(false));
            }
            selectedSubOption = option;
            selectedSubOption.setSelected(new Boolean(true));
            
        break;
        case 3: // nothing to do
        break;
        default:
            throw new SessionInternalError("level not supported:" + level);
        }
        
        
        return retValue;
    }
    
    private void setVector(MenuOption option, Vector toSet) {
        toSet.clear();
        for (Iterator it = option.getOptions().iterator(); it.hasNext(); ) {
            Integer key = (Integer) it.next();
            
            MenuOption subOption = (MenuOption) options.get(key);
            toSet.add(subOption);
        }
        // get these options sorted for display
        Collections.sort(toSet, selectedOption);
    }
}
