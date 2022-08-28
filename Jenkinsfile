pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh './gradlew clean'
            }   
        }    
        
        stage('Build release ') {
            steps {
                sh './gradlew assembleRelease'
            }

            post {
                    always {
                        archiveArtifacts '**/app-release-unsigned.apk', onlyIfSuccessful: true
                    }
            }
        }
    }
}
