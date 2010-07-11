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
import org.gradle.api.tasks.*;
import org.gradle.api.tasks.bundling.Tar;
import org.gradle.api.tasks.bundling.Compression;

/**
 * Extends the Tar task, uploading the created archive to a remote directory,
 * unpacking and deleting it.  Requires Ant ssh (jsch) support.
 *
 * @author ltaylor
 * @author cbeams
 */
class TarUpload extends Tar {
    @Input
    String remoteDir

    @InputFiles
    def classpath

    TarUpload() {
        compression = Compression.BZIP2
    }

    @TaskAction
    void copy() {
        super.copy();
        println "in copy() before upload()"
        upload();
        println "in copy() after upload()"
    }

    def upload() {
        String username = 'cbeams'
        String keyfile = '/Users/cbeams/.ssh/id_rsa'
        String host = 'static.springsource.org'

        project.ant {

            // TODO: get progress metrics by hooking in a listener via Jsch, or ivy resolver, etc.
            taskdef(name: 'scp',
                classname: 'org.apache.tools.ant.taskdefs.optional.ssh.Scp', classpath: classpath.asPath)
            taskdef(name: 'sshexec',
                classname: 'org.apache.tools.ant.taskdefs.optional.ssh.SSHExec', classpath: classpath.asPath)

            // make the remote dir if it doesn't exist yet
            sshexec(host: host, username: username, keyfile: keyfile, command: "mkdir -p $remoteDir")

            // copy the archive, unpack it, then delete it
            def fqRemoteDir = "${username}@${host}:${remoteDir}"
            def unpackCommand = "cd ${remoteDir} && tar -xjf ${archiveName}"
            def deleteCommand = "rm ${remoteDir}/${archiveName}"

            println "scp ${archivePath} -> ${fqRemoteDir}"
            scp(file: archivePath, todir: fqRemoteDir, keyfile: keyfile)
            println "sshexec ${unpackCommand}"
            sshexec(host: host, username: username, keyfile: keyfile, command: unpackCommand)
            println "sshexec ${deleteCommand}"
            sshexec(host: host, username: username, keyfile: keyfile, command: deleteCommand)
        }
    }

    void setLogin(Login login) {
        dependsOn(login)
        this.login = login
    }
}

