package org.springframework.build.gradle.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.*;
import org.gradle.api.tasks.bundling.Tar;
import org.gradle.api.tasks.bundling.Compression;

/**
 * Extends the Tar task, uploading the created archive to a remote directory, unpacking and deleting it.
 * Requires Ant ssh (jsch) support.
 */
class TarUpload extends Tar {
    @Input
    String remoteDir

    Login login

    @InputFiles
    def classpath

    TarUpload() {
        compression = Compression.BZIP2
    }

    @TaskAction
    void copy() {
        super.copy();
        upload();
    }

    def upload() {
        String username = login.username
        String password = login.password
        String host = login.host

        project.ant {
            // TODO: hook in a listener via Jsch, or ivy resolver, etc.
            taskdef(name: 'scp',
                classname: 'org.apache.tools.ant.taskdefs.optional.ssh.Scp', classpath: classpath.asPath)
            taskdef(name: 'sshexec',
                classname: 'org.apache.tools.ant.taskdefs.optional.ssh.SSHExec', classpath: classpath.asPath)

            // make the remote dir if it doesn't exist yet
            sshexec(host: host, username: username, password: password, command: "mkdir -p $remoteDir")

            // copy the archive, unpack it, then delete it
            scp(file: archivePath, todir: "$username@$host:$remoteDir", password: password)
            sshexec(host: host, username: username, password: password, command: "cd $remoteDir && tar -xjf $archiveName")
            sshexec(host: host, username: username, password: password, command: "rm $remoteDir/$archiveName")
        }
    }

    void setLogin(Login login) {
        dependsOn(login)
        this.login = login
    }
}

