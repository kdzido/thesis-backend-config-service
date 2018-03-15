pipeline {
    agent  any
//    {
//        docker 'maven-build-slave-0.1:latest'
//    }

    stages {
        stage('Commit Stage') {
            steps {
                sh './gradlew clean build'
            }
        }
        stage('Acceptance Stage') {
            steps {
                sh './gradlew clean build'
            }
        }
    }

//    post {
//        always {
//             junit 'build/reports/**/*.xml'
//        }
//    }
}
