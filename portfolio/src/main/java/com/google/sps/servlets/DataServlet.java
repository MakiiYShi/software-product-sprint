// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that adds or returns comments.*/
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get comments from datastore.
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("Data").addSort("timestamp", SortDirection.DESCENDING);;
    PreparedQuery results = datastore.prepare(query);

    ArrayList<String> data = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String comment = (String) entity.getProperty("comment");
      String email = (String) entity.getProperty("email");
      data.add(email + " : " + comment);
    }

    // Recode in json and respond.
    response.setContentType("application/json;");
    response.getWriter().println(convertToJsonUsingGson(data));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();

    // Only logged-in users can post messages
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    // Get the input from the form.
    String text = getParameter(request, "text-input", "");
    boolean upperCase = Boolean.parseBoolean(getParameter(request, "upper-case", "false"));
    String email = userService.getCurrentUser().getEmail();

    // Convert the text to upper case.
    if (upperCase) {
      text = text.toUpperCase();
    }

    // Add to the datastore.
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity dataEntity = new Entity("Data");
    dataEntity.setProperty("comment", text);
    dataEntity.setProperty("email", email);
    dataEntity.setProperty("timestamp", System.currentTimeMillis());
    datastore.put(dataEntity);
    // Redirect to the portfolio page.
    response.sendRedirect("/index.html");
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  /**
   * Converts a ArrayList instance into a JSON string using the Gson library. Note: We first added
   * the Gson library dependency to pom.xml.
   */
  private String convertToJsonUsingGson(ArrayList<String> data) {
    Gson gson = new Gson();
    String json = gson.toJson(data);
    return json;
  }
}
