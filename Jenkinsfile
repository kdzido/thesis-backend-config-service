pipeline {
    agent {
        node { label 'docker-enabled' }
    }

    environment {
        PIPELINE_BUILD_ID = "${GIT_BRANCH}-${BUILD_NUMBER}"
        DOCKERHUB_CREDS = credentials("dockerhub")
        // implicit DOCKERHUB_CREDS_USR
        // implicit DOCKERHUB_CREDS_PSW
    }

    stages {
        stage('Unit') {
            steps {
                withEnv(["COMPOSE_FILE=docker-compose-test.yml"]) {
                    sh 'mkdir -p backend-config-service/build/dockerfile'   // dir must exist for docker-compose
                    sh 'docker-compose run --rm unit'
                    sh 'docker-compose build app'
                }
            }
        }
        stage('Staging') {
            steps {
                withEnv(["COMPOSE_FILE=docker-compose-test.yml"]) {
//                    sh 'docker-compose up -d eurekapeer1'
//                    sh 'docker-compose up -d eurekapeer2'
                    sh 'docker-compose run --rm staging'
                }
            }
        }

        stage("Publish") { // Local Docker registry
            steps {
                sh "docker tag thesis-configservice:snapshot localhost:5000/thesis-configservice"
                sh "docker tag thesis-configservice:snapshot localhost:5000/thesis-configervice:${env.BUILD_NUMBER}"
                sh "docker push localhost:5000/thesis-configservice"
                sh "docker push localhost:5000/thesis-configservice:${env.BUILD_NUMBER}"
            }
        }

//        stage("Prod-like") {
//            steps {
//                withEnv([
//                        "DOCKER_TLS_VERIFY=1",
//                        "DOCKER_HOST=tcp://${env.PROD_LIKE_IP}:2376",
//                        "DOCKER_CERT_PATH=/machines/${env.PROD_LIKE_NAME}"]) {
//                    sh "docker service update --image localhost:5000/thesis-configservice:${env.BUILD_NUMBER} configservice"
//                }
//                // TODO smoke test or rollback!!
//                // TODO smoke test or rollback!!
//                // TODO smoke test or rollback!!
//            }
//        }
//        stage("Prod") {
//            steps {
//                withEnv([
//                        "DOCKER_TLS_VERIFY=1",
//                        "DOCKER_HOST=tcp://${env.PROD_IP}:2376",
//                        "DOCKER_CERT_PATH=/machines/${env.PROD_NAME}"]) {
//                    sh "docker service update --image localhost:5000/thesis-configservice:${env.BUILD_NUMBER} configservice"
//                }
//                // TODO smoke test or rollback!!
//                // TODO smoke test or rollback!!
//                // TODO smoke test or rollback!!
//            }
//        }
    }

    post {
        always {
            // TODO handle non-existing backend-config-service/build/dockerfile
            withEnv(["COMPOSE_FILE=docker-compose-test.yml"]) {
                sh "docker-compose down"
            }
        }
    }

}
