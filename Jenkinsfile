pipeline {
    agent any
    
    stages {
        stage('Build') {
        
            steps {

                sh "pwd"
                sh 'ls -al'
                sh './gradlew clean'
            }   
        }    
        
        stage('Build release ') {
            steps {
                
                sh './gradlew assembleRelease'
            }
        }
        
    }
     
    // post {
    //     always {
    //         archiveArtifacts '**/app-release-unsigned.apk', onlyIfSuccessful: true
    //     }
    // }
 
}
