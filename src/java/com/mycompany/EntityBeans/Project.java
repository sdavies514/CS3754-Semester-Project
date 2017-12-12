/*
 * Created by Thomas Corley on 2017.12.04  *
 * Copyright © 2017 Osman Balci. All rights reserved. *
 */
package com.mycompany.EntityBeans;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author tj
 */
@Entity
@Table(name = "Project")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p")
    , @NamedQuery(name = "Project.findById", query = "SELECT p FROM Project p WHERE p.id = :id")
    , @NamedQuery(name = "Project.findByHashedPassword", query = "SELECT p FROM Project p WHERE p.hashedPassword = :hashedPassword")
    , @NamedQuery(name = "Project.findByName", query = "SELECT p FROM Project p WHERE p.name = :name")
    , @NamedQuery(name = "Project.findByRssKey", query = "SELECT p FROM Project p WHERE p.rssKey = :rssKey")})
public class Project implements Serializable {

    @OneToMany(mappedBy = "projectId")
    private Collection<Milestone> milestoneCollection;
    @OneToMany(mappedBy = "projectId")
    private Collection<Activity> activityCollection;
    @OneToMany(mappedBy = "projectId")
    private Collection<ProjectFile> projectFileCollection;
    @OneToMany(mappedBy = "projectId")
    private Collection<UserProjectAssociation> userProjectAssociationCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "hashed_password")
    private String hashedPassword;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "rss_key")
    private String rssKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "projectId")
    private Collection<Message> messageCollection;

    public Project() {
    }

    public Project(Integer id) {
        this.id = id;
    }

    public Project(Integer id, String hashedPassword, String rssKey, String name) {
        this.id = id;
        this.hashedPassword = hashedPassword;
        this.rssKey = rssKey;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getRssKey() {
        return rssKey;
    }

    public void setRssKey(String rssKey) {
        this.rssKey = rssKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<Message> getMessageCollection() {
        return messageCollection;
    }

    public void setMessageCollection(Collection<Message> messageCollection) {
        this.messageCollection = messageCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.EntityBeans.Project[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<Milestone> getMilestoneCollection() {
        return milestoneCollection;
    }

    public void setMilestoneCollection(Collection<Milestone> milestoneCollection) {
        this.milestoneCollection = milestoneCollection;
    }

    @XmlTransient
    public Collection<Activity> getActivityCollection() {
        return activityCollection;
    }

    public void setActivityCollection(Collection<Activity> activityCollection) {
        this.activityCollection = activityCollection;
    }

    @XmlTransient
    public Collection<ProjectFile> getProjectFileCollection() {
        return projectFileCollection;
    }

    public void setProjectFileCollection(Collection<ProjectFile> projectFileCollection) {
        this.projectFileCollection = projectFileCollection;
    }

    @XmlTransient
    public Collection<UserProjectAssociation> getUserProjectAssociationCollection() {
        return userProjectAssociationCollection;
    }

    public void setUserProjectAssociationCollection(Collection<UserProjectAssociation> userProjectAssociationCollection) {
        this.userProjectAssociationCollection = userProjectAssociationCollection;
    }

}
