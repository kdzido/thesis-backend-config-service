pipeline {
    agent  any
//    {
//        docker 'maven-build-slave-0.1:latest'
//    }

    stages {
        stage('Commit Stage') {
            steps {
                sh './gradlew build'
            }
        }
        stage('Acceptance Stage') {
            steps {
//                sh './gradlew :acceptance-test:test'
            }
        }
    }

//    post {
//        always {
//             junit 'build/reports/**/*.xml'
//        }
//    }
}
