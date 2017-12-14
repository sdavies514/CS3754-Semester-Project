/*
 * Created by Casey Butenhoff on 2017.12.06  *
 * Copyright © 2017 Casey Butenhoff. All rights reserved. *
 */
package com.mycompany.rss;

import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author CJ
 */

/*
Identifies the application path that serves as the base URI for all resource
URIs provided by Path. We can't use the '/' path because that's already
reserved for serving our actual web pages, so we must define a sub-path for
web services. Basically, this means that all JAX-RS web services that our
application exposes will be accessible at addressed of the form:

  http://base_url/webresources/endpoint_address

JAX-RS is nice to use in the Java EE environment because it requires no
particular configuration and provides a simple annotation-based interface to
deploy web services with.
*/
@ApplicationPath("webresources")

/*
@ApplicationPath requires that we subclass Application. This class defines a
JAX-RS application, a Java API for RESTful Web Services which uses
annotations to easily turn plain old Java objects (POJOs) into REST
endpoints.
*/
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.mycompany.rss.ActivityFeed.class);
    }

}
