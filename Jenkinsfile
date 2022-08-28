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
        }
    }

    post {
            always {
                archiveArtifacts artifacts: 'app/build/outputs/apk/**/*.apk', fingerprint: true
            }
    }
}
