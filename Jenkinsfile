pipeline {
    agent { node { label 'docker' } }

    environment {
        PIPELINE_BUILD_ID = "${BUILD_TAG}"
    }

    stages {
        stage('Commit Stage') {
            steps {
                sh './gradlew clean build buildDockerImage'
            }
        }
        stage('Acceptance Stage') {
            steps {
                echo 'TODO Acceptance Stage'
            }
        }
    }

//    post {
//        always {
//             junit 'build/reports/**/*.xml'
//        }
//    }
}
