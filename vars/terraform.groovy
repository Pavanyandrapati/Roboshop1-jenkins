def call() {
    pipeline {

        agent {
            node {
                label 'workstation'
            }
        }
        parameters {
            choice(name: 'dev', choices: ['dev', 'prod'], description: 'Pick environment')
        }
        options {
              ansiColor('xterm')
        }
        stages {
            stage('TERRAFORM INIT') {
                steps {
                    sh 'terraform init -backend-config=env-${env}/state.tfvars'
                }
            }
            stage('TERRAFORM APPLY') {
                steps {
                    sh 'terraform apply -auto-approve -var-file=env-${env}/main.tfvars'
                }
            }
        }

        post {
            always {
                cleanws ()
            }
        }


    }
}