/*
 * Created by Casey Butenhoff on 2017.12.06  *
 * Copyright © 2017 Casey Butenhoff. All rights reserved. *
 */
package com.mycompany.rss;

import com.mycompany.EntityBeans.Activity;
import com.mycompany.EntityBeans.Project;
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

/*
JAX-RS provides a number of potentially-useful annotations for mapping POJOs
which represent resource classes to web services. These annotations include:

  @Path - defines the path (relative to @ApplicationPath) of this web service
  @GET - specifies the resource can be accessed using a HTTP GET request
  @PUT - specifies the resource can be accessed using a HTTP PUT request
  @POST - specifies the resource can be accessed using a HTTP POST request
  @DELETE - specifies the resource can be accessed using a HTTP DELETE request
  @HEAD - specifies the resource can be accessed using a HTTP HEAD request
  @Produces - specifies which Internet media types the response may come in
  @Consumes - specifies which Internet media types the request may use

In this case, we're using the @Path annotation because we want the ActivityFeed
resource class to be mapped to a web service at the address:

  http://base_url/webresources/rss

By default, this resource class has a per-request lifecycle, so it's created and
destroyed each time a request to its web service endpoint is made.
*/
@Path("/rss")
public class ActivityFeed {

    /*
    Since we have a different RSS feed for each project and the content of the
    RSS feed contains details of the project, we must inject the ProjectFacade
    in order to resolve the RSS key that was given to the web service to an
    instance of an actual project.
    */
    @EJB
    private com.mycompany.FacadeBeans.ProjectFacade projectFacade;

    /*
    Since the RSS feed pulls all of it's feed data from the Activity table, we
    need to inject the ActivityFacade in order to get that data from the
    database.
    */
    @EJB
    private com.mycompany.FacadeBeans.ActivityFacade activityFacade;

    /*
    In order to generate some of the urls in our RSS feed, we need to access the
    actual request paths, so we must inject the request context which will
    provide them for us.
    */
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ActivityFeed
     */
    public ActivityFeed() {
    }

    /**
     * Retrieves the RSS feed for the project with the given RSS key.
     *
     * @return an RSS feed in XML format containing all the activity that has
     * occurred on the project associated with the given RSS key.
     */
    /*
    We annotate this method with the @GET annotation to indicate that it
    responds to HTTP GET requests.
    */
    @GET
    /*
    We annotate this method with MediaType.APPLICATION_XML passed to the
    @Produces annotation to indicate that its response conforms to the XML
    Internet media type.
    */
    @Produces(MediaType.APPLICATION_XML)
    /*
    The parameter to this method is annotated with @QueryParam("key") to
    indicate that this method accepts an argument in the form of a 'key' query
    parameter in the request path. In other words, requests conforming to a
    path like this:

    http://base_url/webresources/rss?key=foobar

    will pass the value 'foobar' as the value of the key parameter of this
    method.
    */
    public String getXml(@QueryParam("key") String key)
            throws FeedException, NoSuchAlgorithmException {
        List entries = new ArrayList();

        // Since we only have the project's RSS key, we must get the actual
        // project instance in order to retrieve it's name and activity.
        Project project = projectFacade.findByRssKey(key);

        // In case we were given an incorrect project RSS key, we should still
        // return a valid XML response containing a message.
        String feedTitle = "No Activity";
        String feedDescription = "No rss key has been given " +
                "or no activity has been found for the given RSS key";

        if (project != null) {

            // If we actually found the project associated with the given RSS
            // key, update the feed title and description to reflect it.
            feedTitle = project.getName() + " Project Activity";
            feedDescription = "All activity that has occurred in the " +
                project.getName() + " project since it was created.";

            // Iterate over all the activity associated with the project and add
            // it to the RSS feed.
            for (Activity activity : activityFacade.findByProject(project)){

                // This entry class comes from the ROME library, which is very
                // mature, well-documented, and has been the gold-standard for
                // Java RSS feed generation for many years.
                SyndEntry entry = new SyndEntryImpl();

                // The title of each RSS entry is the type of change that the
                // Activity row represents. i.e. INSERT_PROJECT, UPDATE_PROJECT,
                // etc.
                entry.setTitle(activity.getType());

                // Since there is no particular web page associated with this
                // activity, we simply make the entry link back to the feed.
                entry.setLink(context.getRequestUri().toString());

                // Use the timestamp representing when the row was added to the
                // Activity table as the publish date for this entry.
                entry.setPublishedDate(activity.getTimestamp());

                // The content of each RSS entry is simply the message we stored
                // in the Activity table. It's nothing fancy, just plain text,
                // so we set its content type accordingly.
                SyndContent description = new SyndContentImpl();
                description.setType("text/plain");
                description.setValue(activity.getMessage());
                entry.setDescription(description);

                // Add the entry to the collection of entries that the feed will
                // contain.
                entries.add(entry);
            }
        }

        // This feed class comes from the ROME library, which is very mature,
        // well-documented, and has been the gold-standard for Java RSS feed
        // generation for many years.
        SyndFeed feed = new SyndFeedImpl();

        // ROME supports other feed types such as ATOM, but we're currently only
        // interested in providing an RSS 2.0 feed at the moment.
        feed.setFeedType("rss_2.0");

        // This title was generated above and either contains a short title with
        // the project title or an error message depending on if the given RSS
        // key is valid.
        feed.setTitle(feedTitle);

        // This description was generated above and either contains a short
        // description with the project title or an error message depending on
        // if the given RSS key is valid.
        feed.setDescription(feedDescription);

        // Since there is no particular web page associated with this RSS feed,
        // we simply make the feed link back to itself.
        feed.setLink(context.getRequestUri().toString());

        // Actually add all the entries to the RSS feed and return it formatted
        // as an XML document.
        feed.setEntries(entries);
        return new SyndFeedOutput().outputString(feed);
    }
}
