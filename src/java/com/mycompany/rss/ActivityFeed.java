/*
 * Created by Casey Butenhoff on 2017.12.06  *
 * Copyright © 2017 Casey Butenhoff. All rights reserved. *
 */
package com.mycompany.rss;

import com.mycompany.EntityBeans.Activity;
import com.mycompany.EntityBeans.Project;
import com.mycompany.controllers.util.PasswordUtil;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import java.io.IOException;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author CJ
 */
@Path("/rss")
public class ActivityFeed {

    @EJB
    private com.mycompany.FacadeBeans.ProjectFacade projectFacade;
    @EJB
    private com.mycompany.FacadeBeans.ActivityFacade activityFacade;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ActivityFeed
     */
    public ActivityFeed() {
    }

    /**
     * Retrieves representation of an instance of com.mycompany.rss.ActivityFeed
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml(@QueryParam("key") String key)
            throws FeedException, NoSuchAlgorithmException {
        List entries = new ArrayList();
        Project project = projectFacade.findByRssKey(key);
        if (project != null) {
            for (Activity activity : activityFacade.findByProject(project)){
                SyndContent description = new SyndContentImpl();
                description.setType("text/plain");
                description.setValue(activity.getMessage());

                SyndEntry entry = new SyndEntryImpl();
                entry.setTitle(activity.getType());
                entry.setLink("http://wiki.java.net/bin/view/Javawsxml/rome01");
                entry.setPublishedDate(activity.getTimestamp());
                entry.setDescription(description);
                entries.add(entry);
            }
        }

        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle("test-title");
        feed.setDescription("test-description");
        feed.setLink("https://example.org");
        feed.setEntries(entries);
        return new SyndFeedOutput().outputString(feed);
    }

    /**
     * PUT method for updating or creating an instance of ActivityFeed
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
