/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// [START functions_helloworld_get]

package functions;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.sql.DataSource;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

public class HelloWorld implements HttpFunction {
  // Simple function to return "Hello World"
  @Override
  public void service(HttpRequest request, HttpResponse response)
      throws Exception {
	  System.out.println("Request Received");
	  
    BufferedWriter writer = response.getWriter();
    writer.write("Connected to Cloud SQL over SSL and with private IPs");
    ConnectToCloudSql sqlConnect = new ConnectToCloudSql();
    DataSource ds = sqlConnect.createConnectionPool();
    try {
    	sqlConnect.createTable(ds);
    }catch(Exception ie) {
    	ie.printStackTrace();
    	throw ie;
    }
    
  }
}
// [END functions_helloworld_get]
//end
