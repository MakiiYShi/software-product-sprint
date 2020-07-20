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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles authetication and decide login status. */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

  private HashMap<String, String> map = new HashMap<>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
        map.put("IsLogged", "True");
        map.put("Email", userService.getCurrentUser().getEmail());
    } else {
        // message = "<p>Hi stranger! Please login <a href=\"" + loginUrl + "\">here</a>.</p>";
        map.put("IsLogged", "False");
        map.put("Url", userService.createLoginURL("/index.html"));
    }

    response.setContentType("application/json;");
    response.getWriter().println(convertToJsonUsingGson(map));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    // Only logged-in users can post messages
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/login");
      return;
    }
    // Redirect to /index. The request will be routed to the doGet() function above.
    response.sendRedirect("/index.html");
  }

  /**
   * Converts a ArrayList instance into a JSON string using the Gson library. Note: We first added
   * the Gson library dependency to pom.xml.
   */
  private String convertToJsonUsingGson(HashMap<String, String> data) {
    Gson gson = new Gson();
    String json = gson.toJson(data);
    return json;
  }
}
