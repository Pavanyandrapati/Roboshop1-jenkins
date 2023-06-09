def call() {
    pipeline {

        agent {
            node {
                label 'workstation'
            }
        }

        options {
            ansiColor('xterm')
        }

        stages {

            stage('Code Quality') {
                steps {
                //  sh 'ls -l'
                  //  sh 'sonar-scanner -Dsonar.projectKey=${component} -Dsonar.host.url=http://172.31.81.8:9000 -Dsonar.login=admin -Dsonar.password=admin123 -Dsonar.qualitygate.wait=true -Dsonar.java.binaries=./target'
                      sh 'echo Code Quality'
                }
            }

            stage('Unit Test Cases') {
                steps {
                     sh 'echo Unit Test Cases '
                    //sh 'npm test'
                }
            }
            stage('CheckMarx SAST Scan') {
                steps {
                    sh 'echo CheckMarx SAST Scan '
                }
            }
            stage('CheckMarx SCA Scan') {
                steps {
                    sh 'echo CheckMarx SCA Scan '
                }
            }
            stage('Release application') {
                when {
                    expression {
                        env.TAG_NAME ==~ ".*"
                    }
                }
                steps {
                    sh 'echo $TAG_NAME >VERSION'
                    sh 'zip -r ${component}-${TAG_NAME}.zip *'
                    // Deleting this file as it is not needed.
                    sh 'zip -d ${component}-${TAG_NAME}.zip Jenkinsfile'
                    sh sh 'curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${component}-${TAG_NAME}.zip http://172.31.83.153:8081/repository/${component}/${component}-${TAG_NAME}.zip'
                }
            }
        }

        post {
            always {
                cleanWs()
             }
        }

    }


}









