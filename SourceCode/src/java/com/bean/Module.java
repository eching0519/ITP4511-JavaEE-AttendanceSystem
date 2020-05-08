/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bean;

import java.io.Serializable;

/**
 *
 * @author EChing
 */
public class Module implements Serializable {
    private String moduleCode;
    private String moduleTitle;
    private double contactHours;

    public Module() {
    } 

    public Module(String moduleCode, String moduleTitle, double contactHours) {
        this.moduleCode = moduleCode;
        this.moduleTitle = moduleTitle;
        this.contactHours = contactHours;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public void setModuleTitle(String moduleTitle) {
        this.moduleTitle = moduleTitle;
    }

    public double getContactHours() {
        return contactHours;
    }

    public void setContactHours(double contactHours) {
        this.contactHours = contactHours;
    }
}
