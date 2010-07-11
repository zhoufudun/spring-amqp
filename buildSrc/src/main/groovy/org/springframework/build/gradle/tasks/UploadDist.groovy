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
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.TaskAction;


/**
 * Upload distribution zip file to s3.
 *
 * @author ltaylor
 */
class UploadDist extends DefaultTask {
    @InputFile
    File shaFile

    @InputFile
    File archiveFile

    @Input
    String archiveName

    @InputFiles
    def classpath

    @TaskAction
    def upload() {
        def accessKey = project.s3AccessKey
        def secretKey = project.s3SecretAccessKey
        def version = project.version
        def releaseType = project.version.releaseType.toString().toLowerCase()

        project.ant {
            taskdef(resource: 'org/springframework/build/aws/ant/antlib.xml', classpath: classpath.asPath)
            s3(accessKey: accessKey, secretKey: secretKey) {
                upload(bucketName: 'dist.springframework.org', file: archiveFile,
                        toFile: releaseType + "/AMQP/${archiveName}", publicRead: 'true') {
                    metadata(name: 'project.name', value: 'Spring AMQP')
                    metadata(name: 'release.type', value: releaseType)
                    metadata(name: 'bundle.version', value: version)
                    metadata(name: 'package.file.name', value: archiveName)
                }
                upload(bucketName: 'dist.springframework.org', file: shaFile,
                        toFile: releaseType + "/AMQP/${archiveName}.sha1", publicRead: 'true')
            }
        }
    }
}

