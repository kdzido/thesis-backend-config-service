pipeline {
    agent  any
//    {
//        docker 'maven-build-slave-0.1:latest'
//    }

    stages {
        stage('Commit Stage') {
            steps {
                sh './gradlew check'
            }
        }
        stage('Acceptance Stage') {
            steps {
                sh './gradlew :news-mono-acceptance-test:test'
            }
        }
    }

//    post {
//        always {
//             junit 'build/reports/**/*.xml'
//        }
//    }
}
