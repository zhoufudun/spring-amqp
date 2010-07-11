/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.build.gradle.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;


/**
 * Stores login information for a remote host.
 *
 * @author ltaylor
 */
class Login extends DefaultTask {

    String host
    String username
    String password

    @TaskAction
    login() {
        project.ant {
            input("Please enter the ssh username for host '$host'", addproperty: "user.$host")
            input("Please enter the ssh password '$host'", addproperty: "pass.$host")
        }
        username = ant.properties["user.$host"]
        password = ant.properties["pass.$host"]
    }
}
