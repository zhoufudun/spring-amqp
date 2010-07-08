package org.springframework.build.gradle.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.TaskAction;


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

