package org.springframework.build.gradle.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;


/**
 * Stores login information for a remote host.
 */
class Login extends DefaultTask {

    @Input
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
